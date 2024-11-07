package cvds.ProyectoPrestamos.service;

import cvds.ProyectoPrestamos.Exceptions.BookApiException;
import cvds.ProyectoPrestamos.Exceptions.BookLoanException;
import cvds.ProyectoPrestamos.Exceptions.StudentException;
import cvds.ProyectoPrestamos.model.History;
import cvds.ProyectoPrestamos.model.LoanState;
import cvds.ProyectoPrestamos.model.Loans;
import cvds.ProyectoPrestamos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;

import static cvds.ProyectoPrestamos.model.LoanState.Prestado;

/**
 * LoanService handles loan operations for books, including creating, deleting, updating, and returning book loans.
 */
@Service
public class LoanService {

    @Autowired
    loansRepository repository; // Repository for managing loan data.

    @Autowired
    historyRepository historyRepository; // Repository for managing loan history data.

    @Autowired
    private RestTemplate restTemplate; // REST client for making HTTP requests to external services.

    /**
     * Deletes a loan by its ID.
     *
     * @param id the ID of the loan to be deleted.
     * @return an Optional containing the deleted loan, if found.
     */
    public Optional<Loans> deleteLoan(Long id) {
        Optional<Loans> Loan = repository.findById(id);
        repository.deleteById(id);
        return Loan;
    }

    /**
     * Fetches book details from an external API by book ID.
     *
     * @param id the ID of the book to be fetched.
     * @return a JSONObject containing book details.
     * @throws BookApiException if the book data is not found or there is an API error.
     */
    public JSONObject fetchBooks(String id) {
        String url = "https://api.example.com/books/" + id; // Placeholder URL
        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            return new JSONObject(jsonResponse);
        } catch (RestClientException e) {
            throw new BookApiException(BookApiException.ErrorType.DATA_NOT_FOUND, e);
        }
    }

    /**
     * Checks the availability of a book by its ID.
     *
     * @param id the ID of the book.
     * @return the state of the book (e.g., "Available" or "Prestado").
     */
    public String showAvailability(String id) {
        JSONObject jsonObject = fetchBooks(id);
        return jsonObject.getString("state");
    }

    /**
     * Creates a new loan for a book.
     *
     * @param bookCode the code of the book to be loaned.
     * @param studentId the ID of the student requesting the loan.
     * @return the created loan object.
     * @throws BookLoanException if the book is already loaned or if the student has already borrowed it.
     */
    public Loans createLoan(String bookCode, String studentId) throws BookLoanException {
        LoanState state = LoanState.valueOf(showAvailability(bookCode));

        if (state == Prestado) {
            throw new BookLoanException(BookLoanException.ErrorType.ALREADY_BORROWED);
        }

        if (checkStudentHasBook(studentId, bookCode)) {
            throw new StudentException(StudentException.ErrorType.BOOK_ALREADY_ISSUED);
        }

        Loans loan = new Loans(studentId, generateStudentName(studentId), generateReturnDate(bookCode), state, bookCode);
        updateHistory(bookCode, loan.getLoandDate(), loan.getReturnDate(), state, loan.getStudientName());
        updateBookState(bookCode, Prestado);
        repository.save(loan);

        return loan;
    }

    /**
     * Updates the history log with loan details.
     *
     * @param bookCode the code of the book.
     * @param loanDate the date of loan initiation.
     * @param returnDate the expected return date.
     * @param state the current loan state.
     * @param studentName the name of the student borrowing the book.
     */
    public void updateHistory(String bookCode, LocalDate loanDate, LocalDate returnDate, LoanState state, String studentName) {
        History history = new History(bookCode, loanDate, returnDate, state, studentName);
        historyRepository.save(history);
    }

    /**
     * Generates the return date for a loan based on the book category.
     *
     * @param bookCode the code of the book.
     * @return the return date for the loan.
     * @throws BookApiException if the book category is invalid or not found.
     */
    public LocalDate generateReturnDate(String bookCode) {
        LocalDate loanDate = LocalDate.now();
        JSONObject jsonObject = fetchBooks(bookCode);
        String category = jsonObject.getString("category");
        int daysToAdd;

        try {
            Category bookCategory = Category.valueOf(category.toUpperCase());
            switch (bookCategory) {
                case INFANTIL:
                    daysToAdd = 7;
                    break;
                case LITERATURA:
                    daysToAdd = 14;
                    break;
                case NO_FICCION:
                    daysToAdd = 10;
                    break;
                case CUENTOS:
                    daysToAdd = 5;
                    break;
                default:
                    daysToAdd = 18;
                    break;
            }
        } catch (IllegalArgumentException e) {
            throw new BookApiException(BookApiException.ErrorType.DATA_NOT_FOUND, e);
        }

        return loanDate.plusDays(daysToAdd);
    }

    /**
     * Updates the loan history for a book upon return.
     *
     * @param state the updated loan state.
     * @param bookCode the code of the book.
     * @param studentName the name of the student returning the book.
     */
    public void updateHistory(LoanState state, String bookCode, String studentName) {
        History history = new History(bookCode, state, studentName);
        historyRepository.save(history);
    }

    /**
     * Processes the return of a borrowed book.
     *
     * @param loanState the loan state upon return.
     * @param bookCode the code of the book being returned.
     * @param studentId the ID of the student returning the book.
     */
    public void returnBook(LoanState loanState, String bookCode, String studentId) {
        Optional<Loans> loan = repository.findByBookCodeAndStudientId(bookCode, studentId);
        if (loan.isEmpty()) {
            throw new BookLoanException(BookLoanException.ErrorType.NO_LOAN_FOUND);
        }
        repository.delete(loan);
        updateBookState(bookCode, LoanState.Devuelto);
        updateHistory(loanState, bookCode, generateStudentName(studentId));
    }

    /**
     * Retrieves the student's name from an external API by student ID.
     *
     * @param id the ID of the student.
     * @return the student's name.
     * @throws StudentException if the student data is not found or there is an API error.
     */
    public String generateStudentName(String id) {
        String url = "https://api.example.com/studient/" + id;
        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            return jsonObject.getString("name");
        } catch (StudentException e) {
            throw new StudentException(StudentException.ErrorType.STUDENT_NOT_FOUND, e);
        }
    }

    /**
     * Checks if a student currently has a specific book loaned.
     *
     * @param studentId the ID of the student.
     * @param bookCode the code of the book.
     * @return true if the student has the book, false otherwise.
     */
    public boolean checkStudentHasBook(String studentId, String bookCode) {
        Optional<Loans> loan = repository.findByBookCodeAndStudientId(bookCode, studentId);
        return loan.isPresent();
    }

    /**
     * Updates the state of a book in the external API.
     *
     * @param bookCode the code of the book.
     * @param state the new loan state for the book.
     * @throws BookApiException if there is an error updating the book state in the API.
     */
    private void updateBookState(String bookCode, LoanState state) throws BookApiException {
        HttpURLConnection connection = null;

        try {
            URL url = new URL("https://api.example.com/book/" + bookCode);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonInputString = "{\"state\": \"" + state.toString() + "\"}";
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new BookApiException(BookApiException.ErrorType.UPDATE_FAILED);
            }

        } catch (IOException e) {
            throw new BookApiException(BookApiException.ErrorType.API_ERROR, e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Enum representing the various book categories with different return periods.
     */
    public enum Category {
        INFANTIL,
        LITERATURA,
        NO_FICCION,
        CUENTOS
    }
}

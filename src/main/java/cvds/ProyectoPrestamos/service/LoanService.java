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

import java.time.LocalDate;
import java.util.Optional;

import static cvds.ProyectoPrestamos.model.LoanState.Prestado;

@Service
public class LoanService {

    @Autowired
    loansRepository repository;
    @Autowired
    historyRepository historyRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Optional<Loans> deleteLoan(Long id) {
        Optional<Loans> Loan = repository.findById(id);
        repository.deleteById(id);
        return Loan;
    }

    public JSONObject fetchBooks(String id) {
        String url = "https://api.example.com/books/" + id; // URL de prueba
        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            return jsonObject;
        } catch (RestClientException e) {
            throw new BookApiException(BookApiException.ErrorType.DATA_NOT_FOUND, e);
        }
    }

    public String showAvailability(String id) {
        JSONObject jsonObject = fetchBooks(id);
        return jsonObject.getString("availability");
    }

    public Loans createLoan(String bookCode, String studentId) throws BookLoanException {
        LoanState state = LoanState.valueOf(showAvailability(bookCode));

        if (state == Prestado) {
            throw new BookLoanException(BookLoanException.ErrorType.ALREADY_BORROWED);
        }

        Loans loan = new Loans(studentId,generateStudentName(studentId), generateReturnDate(bookCode), state, bookCode);
        updateHistory(bookCode,loan.getLoandDate(),loan.getReturnDate(),state,loan.getStudientName());
        repository.save(loan);

        return loan;
    }


    public void updateHistory(String bookCode, LocalDate loanDate,LocalDate returnDate, LoanState state,String StudientName){
        History history = new History(bookCode,loanDate,returnDate,state,StudientName);
        historyRepository.save(history);
    }


    public LocalDate generateReturnDate(String bookCode) {
        LocalDate loandDate = LocalDate.now();
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

        return loandDate.plusDays(daysToAdd);
    }

    public void updateHistory(String bookCode, LoanState state,String StudientName){
        History history = new History(bookCode,state,StudientName);
        historyRepository.save(history);
    }

    public void returnBook(String state,String bookCode,String studientId) {
        LoanState loanState = LoanState.valueOf(state.toUpperCase());
        updateHistory(bookCode,loanState,generateStudentName(studientId));
    }

    public String generateStudentName(String id){
        String url = "https://api.example.com/studient/" + id;
        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            return jsonObject.getString("name");
        } catch (StudentException e) {
            throw new StudentException(StudentException.ErrorType.STUDENT_NOT_FOUND, e);
        }
    }

    public enum Category {
        INFANTIL,
        LITERATURA,
        NO_FICCION,
        CUENTOS
    }
}

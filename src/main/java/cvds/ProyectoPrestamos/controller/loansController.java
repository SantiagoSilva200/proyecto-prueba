package cvds.ProyectoPrestamos.controller;

import cvds.ProyectoPrestamos.model.Loans;
import cvds.ProyectoPrestamos.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * LoansController handles HTTP requests for loan operations,
 * including creating, deleting, checking availability, and returning loans.
 */
@RestController
@RequestMapping("/loan")
public class loansController {

    @Autowired
    LoanService service;

    /**
     * Creates a new loan for a book based on the request data.
     *
     * @param loan the loan details in the request body.
     * @return the created loan object.
     */
    @PostMapping
    public Loans createLoan(@RequestBody Loans loan) {
        return service.createLoan(loan.getBookCode(), loan.getStudientId());
    }

    /**
     * Deletes an existing loan by its ID.
     *
     * @param id the ID of the loan to delete.
     * @return an Optional containing the deleted loan, if found.
     */
    @DeleteMapping("/{id}")
    public Optional<Loans> deleteLoan(@PathVariable Long id) {
        return service.deleteLoan(id);
    }

    /**
     * Checks the availability status of a book by its ID.
     *
     * @param id the ID of the book to check.
     * @return a string indicating the book's availability.
     */
    @GetMapping("/{id}")
    public String showAvailability(@PathVariable String id) {
        return service.showAvailability(id);
    }

    /**
     * Processes the return of a borrowed book.
     *
     * @param loan the loan details in the request body, including the state, book code, and student ID.
     */
    @PostMapping("/return")
    public void returnBook(@RequestBody Loans loan) {
        service.returnBook(loan.getLoanState(), loan.getBookCode(), loan.getStudientId());
    }
}

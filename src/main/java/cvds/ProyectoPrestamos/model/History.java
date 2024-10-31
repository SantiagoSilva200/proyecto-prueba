package cvds.ProyectoPrestamos.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class History {
    @Column(nullable = false)
    private String bookCode;
    @Column(nullable = false)
    private LocalDate loanDate;
    @Column(nullable = false)
    private LocalDate returnDate;
    @Column(nullable = false)
    private LoanState state;
    @Column(nullable = false)
    private String idUser;

    public History(String bookCode, LocalDate loanDate, LocalDate returnDate, LoanState state, String idUser) {
        this.bookCode = bookCode;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.state = state;
        this.idUser = idUser;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setState(LoanState state) {
        this.state = state;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getBookCode() {
        return bookCode;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public LoanState getState() {
        return state;
    }

    public String getIdUser() {
        return idUser;
    }
}

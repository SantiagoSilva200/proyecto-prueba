package cvds.ProyectoPrestamos.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false)
    private String bookCode;

    @Column(nullable = false)
    private LocalDate loanDate;

    @Column(nullable = false)
    private LocalDate returnDate;

    @Column(nullable = false)
    private LoanState state;

    @Column(nullable = false)
    private String studientName;


    public History(String bookCode, LocalDate loanDate, LocalDate returnDate, LoanState state, String studientName) {
        this.bookCode = bookCode;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.state = state;
        this.studientName = studientName;
    }

    public History(String bookCode, LoanState state, String studientName) {
        this.bookCode = bookCode;
        this.state = state;
        this.studientName = studientName;
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

    public String getStudientName() {
        return studientName;
    }

    public void setStudientName(String studientName) {
        this.studientName = studientName;
    }

}

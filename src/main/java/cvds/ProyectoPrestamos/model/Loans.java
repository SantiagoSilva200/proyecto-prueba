package cvds.ProyectoPrestamos.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;


@Entity
@Getter
@Setter
public class Loans {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    @Column(nullable = false)
    private String bookCode;
    private LocalDate loandDate;
    @Column(nullable = false)
    private LocalDate returnDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanState loanState;
    @Column(nullable = false)
    private String studientId;
    @Column(nullable = false)
    private String studientName;
    @Autowired
    private RestTemplate restTemplate;

    public Loans(String studientId,String studientName,LocalDate returnDate, LoanState loanState,String bookCode) {
        this.returnDate = returnDate;
        this.loanState = loanState;
        this.loandDate = LocalDate.now();
        this.bookCode = bookCode;
        this.studientId = studientId;
        this.studientName = studientName;
    }
    public Long getID() {
        return ID;
    }

    public String getBookCode() {
        return bookCode;
    }

    public LocalDate getLoandDate() {
        return loandDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public LoanState getLoanState() {
        return loanState;
    }

    public String getStudientId() {
        return studientId;
    }

    public String getStudientName() {
        return studientName;
    }
}

package cvds.ProyectoPrestamos.model;


import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

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

    public Loans(LocalDate returnDate, LoanState loanState,String bookCode) {
        this.returnDate = returnDate;
        this.loanState = loanState;
        this.loandDate = LocalDate.now();
        this.bookCode = bookCode;
    }
}

package cvds.ProyectoPrestamos.repository;

import cvds.ProyectoPrestamos.model.Loans;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface loansRepository extends JpaRepository<Loans,Long> {
    Optional<Loans> findByBookCodeAndStudientId(String studientId, String bookCode);
    void delete(Optional<Loans> loan);
}

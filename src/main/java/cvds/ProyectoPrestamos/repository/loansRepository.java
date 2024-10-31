package cvds.ProyectoPrestamos.repository;

import cvds.ProyectoPrestamos.model.Loans;
import org.springframework.data.jpa.repository.JpaRepository;

public interface loansRepository extends JpaRepository<Loans,Long> {

}

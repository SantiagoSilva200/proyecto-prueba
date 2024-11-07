package cvds.ProyectoPrestamos.repository;

import cvds.ProyectoPrestamos.model.History;
import org.springframework.data.jpa.repository.JpaRepository;


public interface historyRepository extends JpaRepository<History, Long> {
}

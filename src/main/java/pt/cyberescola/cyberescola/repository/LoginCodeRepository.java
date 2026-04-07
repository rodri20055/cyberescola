package pt.cyberescola.cyberescola.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.cyberescola.cyberescola.model.LoginCode;

public interface LoginCodeRepository extends JpaRepository<LoginCode, Long> {
    Optional<LoginCode> findTopByEmailAndUsedFalseOrderByIdDesc(String email);
}
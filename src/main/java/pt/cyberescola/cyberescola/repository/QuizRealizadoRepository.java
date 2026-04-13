package pt.cyberescola.cyberescola.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.cyberescola.cyberescola.model.QuizRealizado;

public interface QuizRealizadoRepository extends JpaRepository<QuizRealizado, Long> {

    Optional<QuizRealizado> findByIdUtilizadorAndIdQuiz(Integer idUtilizador, Long idQuiz);
}
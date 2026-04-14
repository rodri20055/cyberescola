package pt.cyberescola.cyberescola.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.cyberescola.cyberescola.model.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByIdConteudo(String idConteudo);
    boolean existsByIdConteudo(String idConteudo);
}
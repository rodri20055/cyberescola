package pt.cyberescola.cyberescola.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.cyberescola.cyberescola.model.QuizTurma;

public interface QuizTurmaRepository extends JpaRepository<QuizTurma, Long> {
    List<QuizTurma> findByIdQuiz(Long idQuiz);
    void deleteByIdQuiz(Long idQuiz);
}
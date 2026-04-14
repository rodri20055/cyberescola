package pt.cyberescola.cyberescola.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.cyberescola.cyberescola.model.PerguntaQuiz;

public interface PerguntaQuizRepository extends JpaRepository<PerguntaQuiz, Long> {
    List<PerguntaQuiz> findByIdQuizOrderByOrdemAsc(Long idQuiz);
    long countByIdQuiz(Long idQuiz);
    void deleteByIdQuiz(Long idQuiz);
}
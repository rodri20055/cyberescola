package pt.cyberescola.cyberescola.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import pt.cyberescola.cyberescola.model.QuizTurma;

public interface QuizTurmaRepository extends JpaRepository<QuizTurma, Long> {

    List<QuizTurma> findByIdQuiz(Long idQuiz);

    List<QuizTurma> findByIdTurma(Long idTurma);

    @Modifying
    @Transactional
    @Query("delete from QuizTurma q where q.idQuiz = :idQuiz")
    void deleteByIdQuiz(Long idQuiz);
}
package pt.cyberescola.cyberescola.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.cyberescola.cyberescola.model.ProfessorTurma;

public interface ProfessorTurmaRepository extends JpaRepository<ProfessorTurma, Long> {
    List<ProfessorTurma> findByIdProfessor(Integer idProfessor);
} 
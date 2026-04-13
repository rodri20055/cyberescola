package pt.cyberescola.cyberescola.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.cyberescola.cyberescola.model.Turma;

public interface TurmaRepository extends JpaRepository<Turma, Long> {
    Optional<Turma> findByNome(String nome);
}
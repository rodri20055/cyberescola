package pt.cyberescola.cyberescola.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.cyberescola.cyberescola.model.ConteudoTurma;

public interface ConteudoTurmaRepository extends JpaRepository<ConteudoTurma, Long> {
    List<ConteudoTurma> findByIdConteudo(Long idConteudo);
    void deleteByIdConteudo(Long idConteudo);
}
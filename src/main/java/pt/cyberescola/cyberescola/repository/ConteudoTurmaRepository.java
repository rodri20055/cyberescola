package pt.cyberescola.cyberescola.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import pt.cyberescola.cyberescola.model.ConteudoTurma;

public interface ConteudoTurmaRepository extends JpaRepository<ConteudoTurma, Long> {

    List<ConteudoTurma> findByIdConteudo(Long idConteudo);

    List<ConteudoTurma> findByIdTurma(Long idTurma);

    @Modifying
    @Transactional
    @Query("delete from ConteudoTurma c where c.idConteudo = :idConteudo")
    void deleteByIdConteudo(Long idConteudo);

    boolean existsByIdConteudoAndIdTurma(Long idConteudo, Long idTurma);
}
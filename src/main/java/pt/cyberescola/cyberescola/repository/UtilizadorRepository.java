package pt.cyberescola.cyberescola.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.cyberescola.cyberescola.model.Utilizador;

public interface UtilizadorRepository extends JpaRepository<Utilizador, Integer> {

    Optional<Utilizador> findByEmailAndPalavraPasse(String email, String palavraPasse);

    Optional<Utilizador> findByEmail(String email);

    List<Utilizador> findAllByOrderByPontosDesc();

    long countByTipo(String tipo);

    long countByTipoAndIdTurmaIn(String tipo, List<Long> idsTurmas);

    List<Utilizador> findByTipoAndIdTurmaInOrderByNomeAsc(String tipo, List<Long> idsTurmas);

    long countByTipoAndIdTurma(String tipo, Long idTurma);

    List<Utilizador> findByTipoAndIdTurmaOrderByNomeAsc(String tipo, Long idTurma);

    List<Utilizador> findByTipoAndIdTurmaOrderByPontosDescNomeAsc(String tipo, Long idTurma);

    
}
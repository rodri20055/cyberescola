package pt.cyberescola.cyberescola.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.cyberescola.cyberescola.model.JogoRealizado;

public interface JogoRealizadoRepository extends JpaRepository<JogoRealizado, Long> {

    List<JogoRealizado> findByIdUtilizadorOrderByDataRealizacaoDesc(Integer idUtilizador);

    long countByIdUtilizador(Integer idUtilizador);

    Optional<JogoRealizado> findTopByIdUtilizadorAndIdJogoOrderByPontuacaoDesc(Integer idUtilizador, Long idJogo);

    long countByIdJogo(Long idJogo);

    List<JogoRealizado> findByIdJogo(Long idJogo);
}
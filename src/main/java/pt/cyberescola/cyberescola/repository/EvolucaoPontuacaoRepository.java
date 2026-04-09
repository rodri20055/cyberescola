package pt.cyberescola.cyberescola.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.cyberescola.cyberescola.model.EvolucaoPontuacao;

public interface EvolucaoPontuacaoRepository extends JpaRepository<EvolucaoPontuacao, Long> {
    List<EvolucaoPontuacao> findByIdUtilizadorOrderByIdAsc(Integer idUtilizador);
}
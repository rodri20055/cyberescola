package pt.cyberescola.cyberescola.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.cyberescola.cyberescola.model.Configuracao;

public interface ConfiguracaoRepository extends JpaRepository<Configuracao, String> {
}
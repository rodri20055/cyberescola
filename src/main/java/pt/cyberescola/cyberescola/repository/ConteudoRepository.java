package pt.cyberescola.cyberescola.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.cyberescola.cyberescola.model.Conteudo;

public interface ConteudoRepository extends JpaRepository<Conteudo, Long> {
    List<Conteudo> findByTemaIgnoreCase(String tema);
}
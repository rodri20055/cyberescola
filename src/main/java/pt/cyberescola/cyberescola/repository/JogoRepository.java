package pt.cyberescola.cyberescola.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.cyberescola.cyberescola.model.Jogo;

public interface JogoRepository extends JpaRepository<Jogo, Long> {

    List<Jogo> findByAtivoTrueOrderByIdAsc();

    List<Jogo> findAllByOrderByIdDesc();
}
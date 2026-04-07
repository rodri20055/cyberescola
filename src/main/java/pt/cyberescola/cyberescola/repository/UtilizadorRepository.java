package pt.cyberescola.cyberescola.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.cyberescola.cyberescola.model.Utilizador;

public interface UtilizadorRepository extends JpaRepository<Utilizador, Integer> {
    Optional<Utilizador> findByEmail(String email);
    Optional<Utilizador> findByEmailAndPalavraPasse(String email, String palavraPasse);
}
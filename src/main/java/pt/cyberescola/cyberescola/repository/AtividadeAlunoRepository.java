package pt.cyberescola.cyberescola.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.cyberescola.cyberescola.model.AtividadeAluno;

public interface AtividadeAlunoRepository extends JpaRepository<AtividadeAluno, Long> {

    List<AtividadeAluno> findTop5ByIdUtilizadorOrderByDataAtividadeDescIdDesc(Integer idUtilizador);

    long countByIdUtilizadorAndTipoAtividade(Integer idUtilizador, String tipoAtividade);

    boolean existsByIdUtilizador(Integer idUtilizador);
}
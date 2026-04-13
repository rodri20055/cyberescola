package pt.cyberescola.cyberescola.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.cyberescola.cyberescola.model.Alerta;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    List<Alerta> findByIdProfessorOrderByDataAlertaDescIdDesc(Integer idProfessor);

    List<Alerta> findByIdProfessorAndTipoOrderByDataAlertaDescIdDesc(Integer idProfessor, String tipo);

    long countByIdProfessorAndLidoFalse(Integer idProfessor);

    long countByIdProfessorAndTipoAndLidoFalse(Integer idProfessor, String tipo);

    boolean existsByIdProfessorAndIdAlunoAndTipoAndTitulo(Integer idProfessor, Integer idAluno, String tipo, String titulo);
}
package pt.cyberescola.cyberescola.model;

import jakarta.persistence.*;

@Entity
@Table(name = "professor_turma")
public class ProfessorTurma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_professor")
    private Integer idProfessor;

    @Column(name = "id_turma")
    private Long idTurma;

    public ProfessorTurma() {
    }

    public Long getId() {
        return id;
    }

    public Integer getIdProfessor() {
        return idProfessor;
    }

    public Long getIdTurma() {
        return idTurma;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdProfessor(Integer idProfessor) {
        this.idProfessor = idProfessor;
    }

    public void setIdTurma(Long idTurma) {
        this.idTurma = idTurma;
    }
}
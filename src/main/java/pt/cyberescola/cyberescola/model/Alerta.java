package pt.cyberescola.cyberescola.model;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "alerta")
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_professor")
    private Integer idProfessor;

    @Column(name = "id_aluno")
    private Integer idAluno;

    @Column(name = "id_turma")
    private Long idTurma;

    private String tipo;
    private String titulo;
    private String descricao;

    @Column(name = "data_alerta")
    private LocalDate dataAlerta;

    private Boolean lido;

    public Alerta() {
    }

    public Long getId() {
        return id;
    }

    public Integer getIdProfessor() {
        return idProfessor;
    }

    public Integer getIdAluno() {
        return idAluno;
    }

    public Long getIdTurma() {
        return idTurma;
    }

    public String getTipo() {
        return tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDate getDataAlerta() {
        return dataAlerta;
    }

    public Boolean getLido() {
        return lido;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdProfessor(Integer idProfessor) {
        this.idProfessor = idProfessor;
    }

    public void setIdAluno(Integer idAluno) {
        this.idAluno = idAluno;
    }

    public void setIdTurma(Long idTurma) {
        this.idTurma = idTurma;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setDataAlerta(LocalDate dataAlerta) {
        this.dataAlerta = dataAlerta;
    }

    public void setLido(Boolean lido) {
        this.lido = lido;
    }
}
package pt.cyberescola.cyberescola.model;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "quiz_realizado")
public class QuizRealizado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_utilizador")
    private Integer idUtilizador;

    @Column(name = "id_quiz")
    private Long idQuiz;

    private Integer pontuacao;

    @Column(name = "data_realizacao")
    private LocalDate dataRealizacao;

    public QuizRealizado() {
    }

    public Long getId() {
        return id;
    }

    public Integer getIdUtilizador() {
        return idUtilizador;
    }

    public Long getIdQuiz() {
        return idQuiz;
    }

    public Integer getPontuacao() {
        return pontuacao;
    }

    public LocalDate getDataRealizacao() {
        return dataRealizacao;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdUtilizador(Integer idUtilizador) {
        this.idUtilizador = idUtilizador;
    }

    public void setIdQuiz(Long idQuiz) {
        this.idQuiz = idQuiz;
    }

    public void setPontuacao(Integer pontuacao) {
        this.pontuacao = pontuacao;
    }

    public void setDataRealizacao(LocalDate dataRealizacao) {
        this.dataRealizacao = dataRealizacao;
    }
}
package pt.cyberescola.cyberescola.model;

import jakarta.persistence.*;

@Entity
@Table(name = "evolucao_pontuacao")
public class EvolucaoPontuacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_utilizador")
    private Integer idUtilizador;

    private String semana;

    private Integer pontos;

    public EvolucaoPontuacao() {
    }

    public Long getId() {
        return id;
    }

    public Integer getIdUtilizador() {
        return idUtilizador;
    }

    public String getSemana() {
        return semana;
    }

    public Integer getPontos() {
        return pontos;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdUtilizador(Integer idUtilizador) {
        this.idUtilizador = idUtilizador;
    }

    public void setSemana(String semana) {
        this.semana = semana;
    }

    public void setPontos(Integer pontos) {
        this.pontos = pontos;
    }
}
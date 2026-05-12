package pt.cyberescola.cyberescola.model;

import jakarta.persistence.*;

@Entity
@Table(name = "jogo")
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String tema;
    private String descricao;
    private String tipo;
    private Boolean ativo;
    private Integer pontosMaximos;
    private Integer tempoLimite;

    @Column(columnDefinition = "TEXT")
    private String dadosJson;

    public Jogo() {
    }

    public Jogo(String titulo, String tema, String descricao, String tipo, Boolean ativo,
                Integer pontosMaximos, Integer tempoLimite, String dadosJson) {
        this.titulo = titulo;
        this.tema = tema;
        this.descricao = descricao;
        this.tipo = tipo;
        this.ativo = ativo;
        this.pontosMaximos = pontosMaximos;
        this.tempoLimite = tempoLimite;
        this.dadosJson = dadosJson;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Integer getPontosMaximos() {
        return pontosMaximos;
    }

    public void setPontosMaximos(Integer pontosMaximos) {
        this.pontosMaximos = pontosMaximos;
    }

    public Integer getTempoLimite() {
        return tempoLimite;
    }

    public void setTempoLimite(Integer tempoLimite) {
        this.tempoLimite = tempoLimite;
    }

    public String getDadosJson() {
        return dadosJson;
    }

    public void setDadosJson(String dadosJson) {
        this.dadosJson = dadosJson;
    }
}
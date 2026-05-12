package pt.cyberescola.cyberescola.model;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "jogo_realizado")
public class JogoRealizado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer idUtilizador;
    private Long idJogo;
    private Integer pontuacao;
    private LocalDate dataRealizacao;
    private Integer tempoSegundos;
    private Integer acertos;
    private Integer erros;

    @Column(columnDefinition = "TEXT")
    private String detalheJson;

    public JogoRealizado() {
    }

    public Long getId() {
        return id;
    }

    public Integer getIdUtilizador() {
        return idUtilizador;
    }

    public void setIdUtilizador(Integer idUtilizador) {
        this.idUtilizador = idUtilizador;
    }

    public Long getIdJogo() {
        return idJogo;
    }

    public void setIdJogo(Long idJogo) {
        this.idJogo = idJogo;
    }

    public Integer getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(Integer pontuacao) {
        this.pontuacao = pontuacao;
    }

    public LocalDate getDataRealizacao() {
        return dataRealizacao;
    }

    public void setDataRealizacao(LocalDate dataRealizacao) {
        this.dataRealizacao = dataRealizacao;
    }

    public Integer getTempoSegundos() {
        return tempoSegundos;
    }

    public void setTempoSegundos(Integer tempoSegundos) {
        this.tempoSegundos = tempoSegundos;
    }

    public Integer getAcertos() {
        return acertos;
    }

    public void setAcertos(Integer acertos) {
        this.acertos = acertos;
    }

    public Integer getErros() {
        return erros;
    }

    public void setErros(Integer erros) {
        this.erros = erros;
    }

    public String getDetalheJson() {
        return detalheJson;
    }

    public void setDetalheJson(String detalheJson) {
        this.detalheJson = detalheJson;
    }
}
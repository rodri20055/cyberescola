package pt.cyberescola.cyberescola.model;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "atividade_aluno")
public class AtividadeAluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_utilizador")
    private Integer idUtilizador;

    @Column(name = "tipo_atividade")
    private String tipoAtividade;

    private String descricao;

    @Column(name = "data_atividade")
    private LocalDate dataAtividade;

    public AtividadeAluno() {
    }

    public Long getId() {
        return id;
    }

    public Integer getIdUtilizador() {
        return idUtilizador;
    }

    public String getTipoAtividade() {
        return tipoAtividade;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDate getDataAtividade() {
        return dataAtividade;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdUtilizador(Integer idUtilizador) {
        this.idUtilizador = idUtilizador;
    }

    public void setTipoAtividade(String tipoAtividade) {
        this.tipoAtividade = tipoAtividade;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setDataAtividade(LocalDate dataAtividade) {
        this.dataAtividade = dataAtividade;
    }
}
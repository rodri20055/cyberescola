package pt.cyberescola.cyberescola.model;

import jakarta.persistence.*;

@Entity
@Table(name = "conteudo_turma")
public class ConteudoTurma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_conteudo")
    private Long idConteudo;

    @Column(name = "id_turma")
    private Long idTurma;

    public Long getId() { return id; }
    public Long getIdConteudo() { return idConteudo; }
    public Long getIdTurma() { return idTurma; }

    public void setId(Long id) { this.id = id; }
    public void setIdConteudo(Long idConteudo) { this.idConteudo = idConteudo; }
    public void setIdTurma(Long idTurma) { this.idTurma = idTurma; }
}
package pt.cyberescola.cyberescola.model;

import jakarta.persistence.*;

@Entity
@Table(name = "quiz")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_conteudo")
    private String idConteudo;

    private String tema;

    private String titulo;

    public Quiz() {
    }

    public Long getId() {
        return id;
    }

    public String getIdConteudo() {
        return idConteudo;
    }

    public String getTema() {
        return tema;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdConteudo(String idConteudo) {
        this.idConteudo = idConteudo;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
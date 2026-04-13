package pt.cyberescola.cyberescola.model;

import jakarta.persistence.*;

@Entity
@Table(name = "conteudo")
public class Conteudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tema;
    private String titulo;
    private String descricao;
    private String duracao;

    @Column(name = "video_url")
    private String videoUrl;

    public Conteudo() {
    }

    public Long getId() {
        return id;
    }

    public String getTema() {
        return tema;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getDuracao() {
        return duracao;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
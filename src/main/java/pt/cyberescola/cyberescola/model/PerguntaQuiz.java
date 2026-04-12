package pt.cyberescola.cyberescola.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pergunta_quiz")
public class PerguntaQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_quiz")
    private Long idQuiz;

    private String enunciado;
    private String opcao1;
    private String opcao2;
    private String opcao3;
    private String opcao4;

    @Column(name = "resposta_correta")
    private String respostaCorreta;

    private Integer ordem;

    public PerguntaQuiz() {
    }

    public Long getId() {
        return id;
    }

    public Long getIdQuiz() {
        return idQuiz;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public String getOpcao1() {
        return opcao1;
    }

    public String getOpcao2() {
        return opcao2;
    }

    public String getOpcao3() {
        return opcao3;
    }

    public String getOpcao4() {
        return opcao4;
    }

    public String getRespostaCorreta() {
        return respostaCorreta;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdQuiz(Long idQuiz) {
        this.idQuiz = idQuiz;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public void setOpcao1(String opcao1) {
        this.opcao1 = opcao1;
    }

    public void setOpcao2(String opcao2) {
        this.opcao2 = opcao2;
    }

    public void setOpcao3(String opcao3) {
        this.opcao3 = opcao3;
    }

    public void setOpcao4(String opcao4) {
        this.opcao4 = opcao4;
    }

    public void setRespostaCorreta(String respostaCorreta) {
        this.respostaCorreta = respostaCorreta;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }
}
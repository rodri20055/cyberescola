package pt.cyberescola.cyberescola.model;

import jakarta.persistence.*;

@Entity
@Table(name = "configuracao")
public class Configuracao {

    @Id
    @Column(name = "chave")
    private String chave;

    @Column(name = "valor")
    private String valor;

    public Configuracao() {
    }

    public Configuracao(String chave, String valor) {
        this.chave = chave;
        this.valor = valor;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
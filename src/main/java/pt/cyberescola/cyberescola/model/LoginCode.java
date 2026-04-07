package pt.cyberescola.cyberescola.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_code")
public class LoginCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String codigo;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    private Boolean used = false;

    @Column(name = "tentativas")
    private Integer tentativas = 0;

    public LoginCode() {
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getCodigo() {
        return codigo;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public Boolean getUsed() {
        return used;
    }

    public Integer getTentativas() {
        return tentativas;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public void setTentativas(Integer tentativas) {
        this.tentativas = tentativas;
    }
}
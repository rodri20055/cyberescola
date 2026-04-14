package pt.cyberescola.cyberescola.model;

import jakarta.persistence.*;

@Entity
@Table(name = "quiz_turma")
public class QuizTurma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_quiz")
    private Long idQuiz;

    @Column(name = "id_turma")
    private Long idTurma;

    public Long getId() { return id; }
    public Long getIdQuiz() { return idQuiz; }
    public Long getIdTurma() { return idTurma; }

    public void setId(Long id) { this.id = id; }
    public void setIdQuiz(Long idQuiz) { this.idQuiz = idQuiz; }
    public void setIdTurma(Long idTurma) { this.idTurma = idTurma; }
}
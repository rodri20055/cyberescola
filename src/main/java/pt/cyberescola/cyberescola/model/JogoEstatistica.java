package pt.cyberescola.cyberescola.model;

public class JogoEstatistica {

    private Long idJogo;
    private long tentativas;
    private int mediaPontuacao;
    private int melhorPontuacao;

    public JogoEstatistica() {
    }

    public JogoEstatistica(Long idJogo, long tentativas, int mediaPontuacao, int melhorPontuacao) {
        this.idJogo = idJogo;
        this.tentativas = tentativas;
        this.mediaPontuacao = mediaPontuacao;
        this.melhorPontuacao = melhorPontuacao;
    }

    public Long getIdJogo() {
        return idJogo;
    }

    public void setIdJogo(Long idJogo) {
        this.idJogo = idJogo;
    }

    public long getTentativas() {
        return tentativas;
    }

    public void setTentativas(long tentativas) {
        this.tentativas = tentativas;
    }

    public int getMediaPontuacao() {
        return mediaPontuacao;
    }

    public void setMediaPontuacao(int mediaPontuacao) {
        this.mediaPontuacao = mediaPontuacao;
    }

    public int getMelhorPontuacao() {
        return melhorPontuacao;
    }

    public void setMelhorPontuacao(int melhorPontuacao) {
        this.melhorPontuacao = melhorPontuacao;
    }
}
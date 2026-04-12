package pt.cyberescola.cyberescola.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;
import pt.cyberescola.cyberescola.model.AtividadeAluno;
import pt.cyberescola.cyberescola.model.EvolucaoPontuacao;
import pt.cyberescola.cyberescola.model.Utilizador;
import pt.cyberescola.cyberescola.repository.AtividadeAlunoRepository;
import pt.cyberescola.cyberescola.repository.EvolucaoPontuacaoRepository;
import pt.cyberescola.cyberescola.repository.UtilizadorRepository;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pt.cyberescola.cyberescola.model.Quiz;
import pt.cyberescola.cyberescola.model.PerguntaQuiz;
import pt.cyberescola.cyberescola.repository.QuizRepository;
import pt.cyberescola.cyberescola.repository.PerguntaQuizRepository;
import java.util.Optional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PaginaController {

    private final UtilizadorRepository utilizadorRepository;
    private final EvolucaoPontuacaoRepository evolucaoPontuacaoRepository;
    private final AtividadeAlunoRepository atividadeAlunoRepository;
    private final QuizRepository quizRepository;
private final PerguntaQuizRepository perguntaQuizRepository;

    public PaginaController(UtilizadorRepository utilizadorRepository,
                        EvolucaoPontuacaoRepository evolucaoPontuacaoRepository,
                        AtividadeAlunoRepository atividadeAlunoRepository,
                        QuizRepository quizRepository,
                        PerguntaQuizRepository perguntaQuizRepository) {
    this.utilizadorRepository = utilizadorRepository;
    this.evolucaoPontuacaoRepository = evolucaoPontuacaoRepository;
    this.atividadeAlunoRepository = atividadeAlunoRepository;
    this.quizRepository = quizRepository;
    this.perguntaQuizRepository = perguntaQuizRepository;
}

    private boolean semLogin(HttpSession session) {
        return session.getAttribute("utilizadorLogado") == null;
    }

    @GetMapping("/aluno")
    public String aluno(HttpSession session, Model model) {
        if (semLogin(session)) return "redirect:/login.html";

        Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
        if (!u.getTipo().equalsIgnoreCase("aluno")) return "redirect:/login.html";

        List<Utilizador> ranking = utilizadorRepository.findAllByOrderByPontosDesc();

        int posicaoRanking = 0;
        for (int i = 0; i < ranking.size(); i++) {
            if (ranking.get(i).getIdUtilizador().equals(u.getIdUtilizador())) {
                posicaoRanking = i + 1;
                break;
            }
        }

        List<EvolucaoPontuacao> evolucao =
                evolucaoPontuacaoRepository.findByIdUtilizadorOrderByIdAsc(u.getIdUtilizador());

        List<String> semanas = evolucao.stream()
                .map(EvolucaoPontuacao::getSemana)
                .collect(Collectors.toList());

        List<Integer> pontosGrafico = evolucao.stream()
                .map(EvolucaoPontuacao::getPontos)
                .collect(Collectors.toList());

        List<AtividadeAluno> atividades =
                atividadeAlunoRepository.findTop5ByIdUtilizadorOrderByDataAtividadeDescIdDesc(u.getIdUtilizador());

        long totalVideos =
                atividadeAlunoRepository.countByIdUtilizadorAndTipoAtividade(u.getIdUtilizador(), "video");

        long totalQuizzes =
                atividadeAlunoRepository.countByIdUtilizadorAndTipoAtividade(u.getIdUtilizador(), "quiz");

        model.addAttribute("utilizador", u);
        model.addAttribute("pontuacaoTotal", u.getPontos() != null ? u.getPontos() : 0);
        model.addAttribute("posicaoRanking", posicaoRanking);
        model.addAttribute("semanasGrafico", semanas);
        model.addAttribute("pontosGrafico", pontosGrafico);
        model.addAttribute("atividades", atividades);
        model.addAttribute("videosVistos", totalVideos);
        model.addAttribute("quizzesFeitos", totalQuizzes);

        System.out.println("ID UTILIZADOR LOGADO: " + u.getIdUtilizador());
        System.out.println("SEMANAS GRAFICO: " + semanas);
        System.out.println("PONTOS GRAFICO: " + pontosGrafico);

        return "aluno";
    }

    @GetMapping("/professor")
    public String professor(HttpSession session) {
        if (semLogin(session)) return "redirect:/login.html";

        Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
        if (!u.getTipo().equalsIgnoreCase("professor")) return "redirect:/login.html";

        return "professor";
    }

    @GetMapping("/admin")
    public String admin(HttpSession session) {
        if (semLogin(session)) return "redirect:/login.html";

        Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
        if (!u.getTipo().equalsIgnoreCase("admin")) return "redirect:/login.html";

        return "admin";
    }

    @GetMapping("/conteudos")
public String conteudos(HttpSession session,
                        Model model,
                        @RequestParam(required = false) String tema,
                        @RequestParam(required = false) String q) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    model.addAttribute("utilizadorLogado", u);

    List<Map<String, String>> conteudos = new ArrayList<>();

    conteudos.add(criarConteudo("1", "Phishing", "O que é Phishing?", "Aprenda a identificar tentativas de phishing e proteja as suas contas.", "5:32"));
    conteudos.add(criarConteudo("2", "Phishing", "Phishing por SMS (Smishing)", "Conheça os perigos do phishing via SMS e como evitá-los.", "4:15"));
    conteudos.add(criarConteudo("3", "Senhas", "Como criar senhas fortes", "Dicas práticas para criar e gerir passwords seguras.", "6:10"));
    conteudos.add(criarConteudo("4", "Senhas", "Gestores de passwords", "Descubra como os gestores de passwords podem proteger a sua vida digital.", "7:45"));
    conteudos.add(criarConteudo("5", "Privacidade", "Privacidade nas redes sociais", "Como configurar a privacidade nas suas redes sociais.", "8:20"));
    conteudos.add(criarConteudo("6", "Privacidade", "Dados pessoais e RGPD", "Entenda os seus direitos sobre os dados pessoais.", "5:55"));

    List<Map<String, String>> conteudosFiltrados = conteudos.stream()
        .filter(c -> tema == null || tema.isBlank() || c.get("tema").equalsIgnoreCase(tema))
        .filter(c -> q == null || q.isBlank()
            || c.get("titulo").toLowerCase().contains(q.toLowerCase())
            || c.get("descricao").toLowerCase().contains(q.toLowerCase()))
        .collect(Collectors.toList());

    model.addAttribute("conteudos", conteudosFiltrados);
    model.addAttribute("temaSelecionado", tema == null ? "" : tema);
    model.addAttribute("pesquisa", q == null ? "" : q);

    return "conteudos";
}

    @GetMapping("/quiz")
    public String quiz(HttpSession session) {
        if (semLogin(session)) return "redirect:/login.html";
        return "quiz";
    }

    @GetMapping("/ranking")
    public String ranking(HttpSession session, Model model) {
        if (semLogin(session)) return "redirect:/login.html";

        Utilizador utilizadorLogado = (Utilizador) session.getAttribute("utilizadorLogado");
        List<Utilizador> ranking = utilizadorRepository.findAllByOrderByPontosDesc();

        model.addAttribute("ranking", ranking);
        model.addAttribute("utilizadorLogado", utilizadorLogado);

        return "ranking";
    }

    @GetMapping("/conteudo/{id}")
    public String detalheConteudo(@PathVariable String id, HttpSession session, Model model) {
        if (semLogin(session)) return "redirect:/login.html";

        Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
        model.addAttribute("utilizadorLogado", u);

        Map<String, String> conteudo = buscarConteudoPorId(id);
        if (conteudo == null) return "redirect:/conteudos";

        model.addAttribute("conteudo", conteudo);

        return "conteudo-detalhe";
    }

    @GetMapping("/quiz-associado/{id}")
public String quizAssociado(@PathVariable String id, HttpSession session, Model model) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    model.addAttribute("utilizadorLogado", u);

    Optional<Quiz> quizOpt = quizRepository.findByIdConteudo(id);
    if (quizOpt.isEmpty()) return "redirect:/conteudos";

    Quiz quiz = quizOpt.get();
    List<PerguntaQuiz> perguntas = perguntaQuizRepository.findByIdQuizOrderByOrdemAsc(quiz.getId());

    model.addAttribute("quiz", quiz);
    model.addAttribute("perguntas", perguntas);

    return "quiz-associado";
}

  @PostMapping("/quiz-associado/{id}")
public String submeterQuiz(@PathVariable String id,
                           @RequestParam String resposta1,
                           @RequestParam String resposta2,
                           @RequestParam String resposta3,
                           @RequestParam String resposta4,
                           @RequestParam String resposta5,
                           HttpSession session,
                           Model model) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    model.addAttribute("utilizadorLogado", u);

    Optional<Quiz> quizOpt = quizRepository.findByIdConteudo(id);
    if (quizOpt.isEmpty()) return "redirect:/conteudos";

    Quiz quiz = quizOpt.get();
    List<PerguntaQuiz> perguntas = perguntaQuizRepository.findByIdQuizOrderByOrdemAsc(quiz.getId());

    model.addAttribute("quiz", quiz);
    model.addAttribute("perguntas", perguntas);

    List<String> respostasUtilizador = List.of(resposta1, resposta2, resposta3, resposta4, resposta5);

    int certas = 0;
    for (int i = 0; i < perguntas.size(); i++) {
        if (respostasUtilizador.get(i).equals(perguntas.get(i).getRespostaCorreta())) {
            certas++;
        }
    }

    int totalPerguntas = perguntas.size();
    int percentagem = (certas * 100) / totalPerguntas;
    int pontosGanhos = certas * 20;

    Integer pontosAtuais = u.getPontos() != null ? u.getPontos() : 0;
u.setPontos(pontosAtuais + pontosGanhos);
utilizadorRepository.save(u);

session.setAttribute("utilizadorLogado", u);

AtividadeAluno atividade = new AtividadeAluno();
atividade.setIdUtilizador(u.getIdUtilizador());
atividade.setTipoAtividade("quiz");
atividade.setDescricao("Completou Quiz: " + quiz.getTema() + " — " + percentagem + "%");
atividade.setDataAtividade(LocalDate.now());

atividadeAlunoRepository.save(atividade);

EvolucaoPontuacao novaEvolucao = new EvolucaoPontuacao();
novaEvolucao.setIdUtilizador(u.getIdUtilizador());
novaEvolucao.setSemana("Quiz " + LocalDate.now());
novaEvolucao.setPontos(u.getPontos());

evolucaoPontuacaoRepository.save(novaEvolucao);

    model.addAttribute("certas", certas);
    model.addAttribute("totalPerguntas", totalPerguntas);
    model.addAttribute("percentagem", percentagem);
    model.addAttribute("pontosGanhos", pontosGanhos);

    return "quiz-resultado";
}

    @GetMapping("/gerir-turmas")
    public String gerirTurmas(HttpSession session) {
        if (semLogin(session)) return "redirect:/login.html";

        Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
        if (!u.getTipo().equalsIgnoreCase("professor")) return "redirect:/login.html";

        return "gerir-turmas";
    }

    @PostMapping("/perfil/editar")
public String editarPerfil(@RequestParam String nome,
                           @RequestParam String email,
                           @RequestParam(required = false) String turma,
                           @RequestParam(required = false) MultipartFile foto,
                           HttpSession session,
                           Model model) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");

    try {
        u.setNome(nome);
        u.setEmail(email);
        u.setTurma(turma);

        if (foto != null && !foto.isEmpty()) {
            String nomeFicheiro = UUID.randomUUID() + "_" + foto.getOriginalFilename();

            Path pastaUploads = Paths.get("src/main/resources/static/uploads");
            Files.createDirectories(pastaUploads);

            Path caminhoFicheiro = pastaUploads.resolve(nomeFicheiro);
            Files.copy(foto.getInputStream(), caminhoFicheiro, StandardCopyOption.REPLACE_EXISTING);

            u.setFotoPerfil("/uploads/" + nomeFicheiro);
        }

        utilizadorRepository.save(u);
        session.setAttribute("utilizadorLogado", u);

        model.addAttribute("utilizador", u);
        model.addAttribute("sucesso", "Perfil atualizado com sucesso.");
        return "perfil";

    } catch (Exception e) {
        model.addAttribute("utilizador", u);
        model.addAttribute("erro", "Ocorreu um erro ao atualizar o perfil.");
        return "perfil";
    }
}

@PostMapping("/perfil/password")
public String alterarPassword(@RequestParam String passwordAtual,
                              @RequestParam String novaPassword,
                              @RequestParam String confirmarPassword,
                              HttpSession session,
                              Model model) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    model.addAttribute("utilizador", u);

    if (!u.getPalavraPasse().equals(passwordAtual)) {
        model.addAttribute("erro", "A palavra-passe atual está incorreta.");
        return "perfil";
    }

    if (!novaPassword.equals(confirmarPassword)) {
        model.addAttribute("erro", "A confirmação da nova palavra-passe não coincide.");
        return "perfil";
    }

    if (novaPassword.length() < 6) {
        model.addAttribute("erro", "A nova palavra-passe deve ter pelo menos 6 caracteres.");
        return "perfil";
    }

    u.setPalavraPasse(novaPassword);
    utilizadorRepository.save(u);
    session.setAttribute("utilizadorLogado", u);

    model.addAttribute("utilizador", u);
    model.addAttribute("sucesso", "Palavra-passe alterada com sucesso.");
    return "perfil";
}

    @GetMapping("/perfil")
public String perfil(HttpSession session, Model model) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    model.addAttribute("utilizador", u);

    return "perfil";
}

    private Map<String, String> criarConteudo(String id, String tema, String titulo, String descricao, String duracao) {
        Map<String, String> c = new HashMap<>();
        c.put("id", id);
        c.put("tema", tema);
        c.put("titulo", titulo);
        c.put("descricao", descricao);
        c.put("duracao", duracao);
        c.put("videoUrl", "https://www.youtube.com/embed/dQw4w9WgXcQ");
        return c;
    }

    private Map<String, String> buscarConteudoPorId(String id) {
        List<Map<String, String>> conteudos = new ArrayList<>();

        conteudos.add(criarConteudo("1", "Phishing", "O que é Phishing?", "Aprenda a identificar tentativas de phishing e proteja as suas contas.", "5:32"));
        conteudos.add(criarConteudo("2", "Phishing", "Phishing por SMS (Smishing)", "Conheça os perigos do phishing via SMS e como evitá-los.", "4:15"));
        conteudos.add(criarConteudo("3", "Senhas", "Como criar senhas fortes", "Dicas práticas para criar e gerir passwords seguras.", "6:10"));
        conteudos.add(criarConteudo("4", "Senhas", "Gestores de passwords", "Descubra como os gestores de passwords podem proteger a sua vida digital.", "7:45"));
        conteudos.add(criarConteudo("5", "Privacidade", "Privacidade nas redes sociais", "Como configurar a privacidade nas suas redes sociais.", "8:20"));
        conteudos.add(criarConteudo("6", "Privacidade", "Dados pessoais e RGPD", "Entenda os seus direitos sobre os dados pessoais.", "5:55"));

        for (Map<String, String> c : conteudos) {
            if (c.get("id").equals(id)) return c;
        }

        return null;
    }

    private Map<String, Object> buscarQuizPorConteudo(String id) {
        Map<String, Object> quiz = new HashMap<>();

        switch (id) {
            case "1":
                quiz.put("id", "1");
                quiz.put("tema", "Phishing");
                quiz.put("pergunta", "O que é phishing?");
                quiz.put("opcao1", "Um vírus");
                quiz.put("opcao2", "Uma tentativa de obter dados pessoais");
                quiz.put("opcao3", "Um tipo de firewall");
                quiz.put("opcao4", "Uma rede social");
                break;

            case "2":
                quiz.put("id", "2");
                quiz.put("tema", "Phishing");
                quiz.put("pergunta", "O que é smishing?");
                quiz.put("opcao1", "Phishing por SMS");
                quiz.put("opcao2", "Um antivírus");
                quiz.put("opcao3", "Um cabo de rede");
                quiz.put("opcao4", "Uma atualização do sistema");
                break;

            case "3":
                quiz.put("id", "3");
                quiz.put("tema", "Senhas");
                quiz.put("pergunta", "Uma senha forte deve:");
                quiz.put("opcao1", "Ser curta e fácil");
                quiz.put("opcao2", "Ter letras, números e símbolos");
                quiz.put("opcao3", "Ser o teu nome");
                quiz.put("opcao4", "Ser sempre 123456");
                break;

            case "4":
                quiz.put("id", "4");
                quiz.put("tema", "Senhas");
                quiz.put("pergunta", "Um gestor de passwords serve para:");
                quiz.put("opcao1", "Apagar ficheiros");
                quiz.put("opcao2", "Guardar senhas em segurança");
                quiz.put("opcao3", "Criar vírus");
                quiz.put("opcao4", "Acelerar a internet");
                break;

            case "5":
                quiz.put("id", "5");
                quiz.put("tema", "Privacidade");
                quiz.put("pergunta", "Nas redes sociais deves:");
                quiz.put("opcao1", "Partilhar tudo publicamente");
                quiz.put("opcao2", "Configurar a privacidade da conta");
                quiz.put("opcao3", "Aceitar qualquer pedido");
                quiz.put("opcao4", "Mostrar todos os dados pessoais");
                break;

            case "6":
                quiz.put("id", "6");
                quiz.put("tema", "Privacidade");
                quiz.put("pergunta", "O RGPD está relacionado com:");
                quiz.put("opcao1", "Jogos online");
                quiz.put("opcao2", "Proteção de dados pessoais");
                quiz.put("opcao3", "Passwords fracas");
                quiz.put("opcao4", "Redes Wi-Fi");
                break;

            default:
                return null;
        }

        return quiz;
    }

    private String buscarRespostaCorreta(String id) {
    switch (id) {
        case "1":
            return "Uma tentativa de obter dados pessoais";
        case "2":
            return "Phishing por SMS";
        case "3":
            return "Ter letras, números e símbolos";
        case "4":
            return "Guardar senhas em segurança";
        case "5":
            return "Configurar a privacidade da conta";
        case "6":
            return "Proteção de dados pessoais";
        default:
            return "";
    }
}
}
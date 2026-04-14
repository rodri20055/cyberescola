package pt.cyberescola.cyberescola.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import pt.cyberescola.cyberescola.model.Alerta;
import pt.cyberescola.cyberescola.model.AtividadeAluno;
import pt.cyberescola.cyberescola.model.Conteudo;
import pt.cyberescola.cyberescola.model.ConteudoTurma;
import pt.cyberescola.cyberescola.model.EvolucaoPontuacao;
import pt.cyberescola.cyberescola.model.PerguntaQuiz;
import pt.cyberescola.cyberescola.model.ProfessorTurma;
import pt.cyberescola.cyberescola.model.Quiz;
import pt.cyberescola.cyberescola.model.QuizRealizado;
import pt.cyberescola.cyberescola.model.QuizTurma;
import pt.cyberescola.cyberescola.model.Turma;
import pt.cyberescola.cyberescola.model.Utilizador;
import pt.cyberescola.cyberescola.repository.AlertaRepository;
import pt.cyberescola.cyberescola.repository.AtividadeAlunoRepository;
import pt.cyberescola.cyberescola.repository.ConteudoRepository;
import pt.cyberescola.cyberescola.repository.ConteudoTurmaRepository;
import pt.cyberescola.cyberescola.repository.EvolucaoPontuacaoRepository;
import pt.cyberescola.cyberescola.repository.PerguntaQuizRepository;
import pt.cyberescola.cyberescola.repository.ProfessorTurmaRepository;
import pt.cyberescola.cyberescola.repository.QuizRealizadoRepository;
import pt.cyberescola.cyberescola.repository.QuizRepository;
import pt.cyberescola.cyberescola.repository.QuizTurmaRepository;
import pt.cyberescola.cyberescola.repository.TurmaRepository;
import pt.cyberescola.cyberescola.repository.UtilizadorRepository;

@Controller
public class PaginaController {

    private final UtilizadorRepository utilizadorRepository;
    private final EvolucaoPontuacaoRepository evolucaoPontuacaoRepository;
    private final AtividadeAlunoRepository atividadeAlunoRepository;
    private final QuizRepository quizRepository;
    private final PerguntaQuizRepository perguntaQuizRepository;
    private final ConteudoRepository conteudoRepository;
    private final QuizRealizadoRepository quizRealizadoRepository;
    private final TurmaRepository turmaRepository;
    private final ProfessorTurmaRepository professorTurmaRepository;
    private final AlertaRepository alertaRepository;
    private final ConteudoTurmaRepository conteudoTurmaRepository;
    private final QuizTurmaRepository quizTurmaRepository;

    public PaginaController(UtilizadorRepository utilizadorRepository,
                            EvolucaoPontuacaoRepository evolucaoPontuacaoRepository,
                            AtividadeAlunoRepository atividadeAlunoRepository,
                            QuizRepository quizRepository,
                            PerguntaQuizRepository perguntaQuizRepository,
                            ConteudoRepository conteudoRepository,
                            QuizRealizadoRepository quizRealizadoRepository,
                            TurmaRepository turmaRepository,
                            ProfessorTurmaRepository professorTurmaRepository,
                            AlertaRepository alertaRepository,
                            ConteudoTurmaRepository conteudoTurmaRepository,
                            QuizTurmaRepository quizTurmaRepository) {
        this.utilizadorRepository = utilizadorRepository;
        this.evolucaoPontuacaoRepository = evolucaoPontuacaoRepository;
        this.atividadeAlunoRepository = atividadeAlunoRepository;
        this.quizRepository = quizRepository;
        this.perguntaQuizRepository = perguntaQuizRepository;
        this.conteudoRepository = conteudoRepository;
        this.quizRealizadoRepository = quizRealizadoRepository;
        this.turmaRepository = turmaRepository;
        this.professorTurmaRepository = professorTurmaRepository;
        this.alertaRepository = alertaRepository;
        this.conteudoTurmaRepository = conteudoTurmaRepository;
        this.quizTurmaRepository = quizTurmaRepository;
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
public String professor(HttpSession session, Model model) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("professor")) return "redirect:/login.html";

    List<ProfessorTurma> ligacoes = professorTurmaRepository.findByIdProfessor(u.getIdUtilizador());
    List<Long> idsTurmas = ligacoes.stream()
            .map(ProfessorTurma::getIdTurma)
            .toList();

    List<Turma> turmasProfessor = turmaRepository.findAllById(idsTurmas);
    long totalTurmas = turmasProfessor.size();
    long totalAlunos = idsTurmas.isEmpty() ? 0 : utilizadorRepository.countByTipoAndIdTurmaIn("aluno", idsTurmas);
    long totalConteudos = conteudoRepository.count();
    long totalQuizzes = quizRepository.count();

    List<Utilizador> alunos = idsTurmas.isEmpty()
            ? List.of()
            : utilizadorRepository.findByTipoAndIdTurmaInOrderByNomeAsc("aluno", idsTurmas);

    model.addAttribute("utilizador", u);
    model.addAttribute("turmasProfessor", turmasProfessor);
    model.addAttribute("totalTurmas", totalTurmas);
    model.addAttribute("totalAlunos", totalAlunos);
    model.addAttribute("totalConteudos", totalConteudos);
    model.addAttribute("totalQuizzes", totalQuizzes);
    model.addAttribute("alunos", alunos);

    return "professor";
}

   @GetMapping("/admin")
public String admin(HttpSession session, Model model) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("admin")) return "redirect:/login.html";

    List<Utilizador> utilizadores = utilizadorRepository.findAll();

    long totalUtilizadores = utilizadores.size();
    long totalAlunos = utilizadores.stream()
            .filter(x -> x.getTipo() != null && x.getTipo().equalsIgnoreCase("aluno"))
            .count();

    long totalProfessores = utilizadores.stream()
            .filter(x -> x.getTipo() != null && x.getTipo().equalsIgnoreCase("professor"))
            .count();

    long totalAdmins = utilizadores.stream()
            .filter(x -> x.getTipo() != null && x.getTipo().equalsIgnoreCase("admin"))
            .count();

    long totalTurmas = turmaRepository.count();
    long totalConteudos = conteudoRepository.count();
    long totalQuizzes = quizRepository.count();

    model.addAttribute("utilizador", u);
    model.addAttribute("utilizadores", utilizadores);
    model.addAttribute("totalUtilizadores", totalUtilizadores);
    model.addAttribute("totalAlunos", totalAlunos);
    model.addAttribute("totalProfessores", totalProfessores);
    model.addAttribute("totalAdmins", totalAdmins);
    model.addAttribute("totalTurmas", totalTurmas);
    model.addAttribute("totalConteudos", totalConteudos);
    model.addAttribute("totalQuizzes", totalQuizzes);

    return "admin";
}

@GetMapping("/admin/utilizadores")
public String adminUtilizadores(HttpSession session, Model model) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("admin")) return "redirect:/login.html";

    List<Utilizador> utilizadores = utilizadorRepository.findAll();

    model.addAttribute("utilizador", u);
    model.addAttribute("utilizadores", utilizadores);

    return "admin-utilizadores";
}
    @GetMapping("/conteudos")
public String conteudos(HttpSession session,
                        Model model,
                        @RequestParam(required = false) String tema,
                        @RequestParam(required = false) String q) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    model.addAttribute("utilizadorLogado", u);

    List<Conteudo> conteudos = conteudoRepository.findAll();

    List<Conteudo> conteudosFiltrados = conteudos.stream()
        .filter(c -> tema == null || tema.isBlank() || c.getTema().equalsIgnoreCase(tema))
        .filter(c -> q == null || q.isBlank()
            || c.getTitulo().toLowerCase().contains(q.toLowerCase())
            || c.getDescricao().toLowerCase().contains(q.toLowerCase()))
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

    Long conteudoId = Long.parseLong(id);
    Conteudo conteudo = conteudoRepository.findById(conteudoId).orElse(null);

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

    boolean jaRealizou = quizRealizadoRepository
        .findByIdUtilizadorAndIdQuiz(u.getIdUtilizador(), quiz.getId())
        .isPresent();

if (jaRealizou) {
    pontosGanhos = 0;
}

    if (!jaRealizou) {
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

    QuizRealizado realizado = new QuizRealizado();
    realizado.setIdUtilizador(u.getIdUtilizador());
    realizado.setIdQuiz(quiz.getId());
    realizado.setPontuacao(percentagem);
    realizado.setDataRealizacao(LocalDate.now());
    quizRealizadoRepository.save(realizado);
} else {
    session.setAttribute("utilizadorLogado", u);
}

    model.addAttribute("certas", certas);
    model.addAttribute("totalPerguntas", totalPerguntas);
    model.addAttribute("percentagem", percentagem);
    model.addAttribute("pontosGanhos", pontosGanhos);

    model.addAttribute("jaRealizou", jaRealizou);

    return "quiz-resultado";
}

@PostMapping("/admin/utilizadores/editar/{id}")
public String editarUtilizadorAdmin(@PathVariable Integer id,
                                    @RequestParam String nome,
                                    @RequestParam String email,
                                    @RequestParam String tipo,
                                    @RequestParam(required = false) String turma,
                                    @RequestParam(required = false, defaultValue = "false") boolean ativo,
                                    HttpSession session) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador admin = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!admin.getTipo().equalsIgnoreCase("admin")) return "redirect:/login.html";

    Utilizador utilizador = utilizadorRepository.findById(id).orElse(null);
    if (utilizador == null) return "redirect:/admin/utilizadores";

    utilizador.setNome(nome);
    utilizador.setEmail(email);
    utilizador.setTipo(tipo);
    utilizador.setTurma(turma);
    utilizador.setAtivo(ativo);

    utilizadorRepository.save(utilizador);

    return "redirect:/admin/utilizadores";
}

@GetMapping("/admin/conteudos")
public String adminConteudos(@RequestParam(required = false) String tipo,
                             HttpSession session,
                             Model model) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("admin")) return "redirect:/login.html";

    String tipoSelecionado = (tipo == null || tipo.isBlank()) ? "videos" : tipo;

    List<Conteudo> conteudos = conteudoRepository.findAllByOrderByIdDesc();
    List<Quiz> quizzes = quizRepository.findAll();

    model.addAttribute("utilizador", u);
    model.addAttribute("conteudos", conteudos);
    model.addAttribute("quizzes", quizzes);
    model.addAttribute("tipoSelecionado", tipoSelecionado);

    return "admin-conteudos";
}

@GetMapping("/admin/configuracao")
public String adminConfiguracao(HttpSession session, Model model) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("admin")) return "redirect:/login.html";

    model.addAttribute("utilizador", u);

    return "admin-configuracao";
}


@GetMapping("/admin/perfil")
public String adminPerfil(HttpSession session, Model model) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("admin")) return "redirect:/login.html";

    model.addAttribute("utilizador", u);

    return "admin-perfil";
}

@PostMapping("/admin/utilizadores/apagar/{id}")
public String apagarUtilizadorAdmin(@PathVariable Integer id,
                                    HttpSession session) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador admin = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!admin.getTipo().equalsIgnoreCase("admin")) return "redirect:/login.html";

    Utilizador utilizador = utilizadorRepository.findById(id).orElse(null);
    if (utilizador == null) return "redirect:/admin/utilizadores";

    if (utilizador.getIdUtilizador().equals(admin.getIdUtilizador())) {
        return "redirect:/admin/utilizadores";
    }

    utilizadorRepository.deleteById(id);

    return "redirect:/admin/utilizadores";
}

    @GetMapping("/gerir-turmas")
public String gerirTurmas(HttpSession session, Model model) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("professor")) return "redirect:/login.html";

    List<ProfessorTurma> ligacoes = professorTurmaRepository.findByIdProfessor(u.getIdUtilizador());
    List<Long> idsTurmas = ligacoes.stream()
            .map(ProfessorTurma::getIdTurma)
            .toList();

    List<Turma> turmasProfessor = turmaRepository.findAllById(idsTurmas);

    List<Map<String, Object>> turmasComResumo = turmasProfessor.stream().map(turma -> {
        Map<String, Object> item = new java.util.HashMap<>();
        item.put("id", turma.getId());
        item.put("nome", turma.getNome());
        item.put("totalAlunos", utilizadorRepository.countByTipoAndIdTurma("aluno", turma.getId()));
        return item;
    }).toList();

    model.addAttribute("utilizador", u);
    model.addAttribute("turmasProfessor", turmasComResumo);

    return "gerir-turmas";
}

@GetMapping("/turma/{id}")
public String detalheTurma(@PathVariable Long id,
                           @RequestParam(required = false) String q,
                           HttpSession session,
                           Model model) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("professor")) return "redirect:/login.html";

    List<ProfessorTurma> ligacoes = professorTurmaRepository.findByIdProfessor(u.getIdUtilizador());
    List<Long> idsTurmasProfessor = ligacoes.stream()
            .map(ProfessorTurma::getIdTurma)
            .toList();

    if (!idsTurmasProfessor.contains(id)) return "redirect:/gerir-turmas";

    Turma turma = turmaRepository.findById(id).orElse(null);
    if (turma == null) return "redirect:/gerir-turmas";

   List<Utilizador> alunos = utilizadorRepository.findByTipoAndIdTurmaOrderByPontosDescNomeAsc("aluno", id);

    if (q != null && !q.trim().isEmpty()) {
        String pesquisa = q.trim().toLowerCase();
        alunos = alunos.stream()
                .filter(a -> a.getNome() != null && a.getNome().toLowerCase().contains(pesquisa))
                .toList();
    }

    int totalAlunos = alunos.size();

    double mediaPontos = alunos.stream()
            .map(a -> a.getPontos() != null ? a.getPontos() : 0)
            .mapToInt(Integer::intValue)
            .average()
            .orElse(0);

    Utilizador melhorAluno = alunos.stream()
            .max((a1, a2) -> Integer.compare(
                    a1.getPontos() != null ? a1.getPontos() : 0,
                    a2.getPontos() != null ? a2.getPontos() : 0))
            .orElse(null);

    model.addAttribute("utilizador", u);
    model.addAttribute("turma", turma);
    model.addAttribute("alunos", alunos);
    model.addAttribute("pesquisa", q == null ? "" : q);
    model.addAttribute("totalAlunos", totalAlunos);
    model.addAttribute("mediaPontos", Math.round(mediaPontos));
    model.addAttribute("melhorAluno", melhorAluno);

    return "detalhe-turma";
}

@GetMapping("/relatorios")
public String relatorios(HttpSession session, Model model) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("professor")) return "redirect:/login.html";

    List<ProfessorTurma> ligacoes = professorTurmaRepository.findByIdProfessor(u.getIdUtilizador());
    List<Long> idsTurmas = ligacoes.stream()
            .map(ProfessorTurma::getIdTurma)
            .toList();

    List<Turma> turmasProfessor = turmaRepository.findAllById(idsTurmas);

    List<Utilizador> alunos = idsTurmas.isEmpty()
            ? List.of()
            : utilizadorRepository.findByTipoAndIdTurmaInOrderByNomeAsc("aluno", idsTurmas);

    List<String> nomesTurmas = new ArrayList<>();
    List<Long> totalAlunosPorTurma = new ArrayList<>();
    List<Long> mediaPontosPorTurma = new ArrayList<>();

    for (Turma turma : turmasProfessor) {
        List<Utilizador> alunosTurma = utilizadorRepository.findByTipoAndIdTurmaOrderByNomeAsc("aluno", turma.getId());

        long total = alunosTurma.size();

        long media = Math.round(
                alunosTurma.stream()
                        .map(a -> a.getPontos() != null ? a.getPontos() : 0)
                        .mapToInt(Integer::intValue)
                        .average()
                        .orElse(0)
        );

        nomesTurmas.add(turma.getNome());
        totalAlunosPorTurma.add(total);
        mediaPontosPorTurma.add(media);
    }

    long totalAlunos = alunos.size();

    long mediaGlobal = Math.round(
            alunos.stream()
                    .map(a -> a.getPontos() != null ? a.getPontos() : 0)
                    .mapToInt(Integer::intValue)
                    .average()
                    .orElse(0)
    );

    Turma melhorTurma = null;
    long melhorMedia = 0;

    for (int i = 0; i < turmasProfessor.size(); i++) {
        if (mediaPontosPorTurma.get(i) > melhorMedia) {
            melhorMedia = mediaPontosPorTurma.get(i);
            melhorTurma = turmasProfessor.get(i);
        }
    }

    List<String> meses = List.of("Jan", "Fev", "Mar", "Abr");
    List<Integer> evolucaoMedia = List.of(
            (int) Math.max(0, mediaGlobal - 18),
            (int) Math.max(0, mediaGlobal - 10),
            (int) Math.max(0, mediaGlobal - 5),
            (int) mediaGlobal
    );

    model.addAttribute("utilizador", u);
    model.addAttribute("nomesTurmas", nomesTurmas);
    model.addAttribute("totalAlunosPorTurma", totalAlunosPorTurma);
    model.addAttribute("mediaPontosPorTurma", mediaPontosPorTurma);
    model.addAttribute("totalAlunos", totalAlunos);
    model.addAttribute("totalTurmas", turmasProfessor.size());
    model.addAttribute("mediaGlobal", mediaGlobal);
    model.addAttribute("melhorTurma", melhorTurma != null ? melhorTurma.getNome() : "Sem dados");
    model.addAttribute("melhorMedia", melhorMedia);
    model.addAttribute("meses", meses);
    model.addAttribute("evolucaoMedia", evolucaoMedia);

    return "relatorios";
}


@GetMapping("/alertas")
public String alertas(@RequestParam(required = false) String tipo,
                      HttpSession session,
                      Model model) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("professor")) return "redirect:/login.html";

    List<ProfessorTurma> ligacoes = professorTurmaRepository.findByIdProfessor(u.getIdUtilizador());
    List<Long> idsTurmas = ligacoes.stream()
            .map(ProfessorTurma::getIdTurma)
            .toList();

    List<Utilizador> alunos = idsTurmas.isEmpty()
            ? List.of()
            : utilizadorRepository.findByTipoAndIdTurmaInOrderByNomeAsc("aluno", idsTurmas);

    for (Utilizador aluno : alunos) {
        int pontos = aluno.getPontos() != null ? aluno.getPontos() : 0;

        if (pontos < 100) {
            String titulo = "Pontuação baixa";
            if (!alertaRepository.existsByIdProfessorAndIdAlunoAndTipoAndTitulo(
                    u.getIdUtilizador(), aluno.getIdUtilizador(), "desempenho", titulo)) {

                Alerta alerta = new Alerta();
                alerta.setIdProfessor(u.getIdUtilizador());
                alerta.setIdAluno(aluno.getIdUtilizador());
                alerta.setIdTurma(aluno.getIdTurma());
                alerta.setTipo("desempenho");
                alerta.setTitulo(titulo);
                alerta.setDescricao(aluno.getNome() + " está abaixo dos 100 pontos.");
                alerta.setDataAlerta(java.time.LocalDate.now());
                alerta.setLido(false);
                alertaRepository.save(alerta);
            }
        }

        long quizzesFeitos = atividadeAlunoRepository.countByIdUtilizadorAndTipoAtividade(aluno.getIdUtilizador(), "quiz");
        if (quizzesFeitos == 0) {
            String titulo = "Quiz por realizar";
            if (!alertaRepository.existsByIdProfessorAndIdAlunoAndTipoAndTitulo(
                    u.getIdUtilizador(), aluno.getIdUtilizador(), "pendencia", titulo)) {

                Alerta alerta = new Alerta();
                alerta.setIdProfessor(u.getIdUtilizador());
                alerta.setIdAluno(aluno.getIdUtilizador());
                alerta.setIdTurma(aluno.getIdTurma());
                alerta.setTipo("pendencia");
                alerta.setTitulo(titulo);
                alerta.setDescricao(aluno.getNome() + " ainda não realizou nenhum quiz.");
                alerta.setDataAlerta(java.time.LocalDate.now());
                alerta.setLido(false);
                alertaRepository.save(alerta);
            }
        }

        boolean temAtividade = atividadeAlunoRepository.existsByIdUtilizador(aluno.getIdUtilizador());
        if (!temAtividade) {
            String titulo = "Sem atividade";
            if (!alertaRepository.existsByIdProfessorAndIdAlunoAndTipoAndTitulo(
                    u.getIdUtilizador(), aluno.getIdUtilizador(), "atividade", titulo)) {

                Alerta alerta = new Alerta();
                alerta.setIdProfessor(u.getIdUtilizador());
                alerta.setIdAluno(aluno.getIdUtilizador());
                alerta.setIdTurma(aluno.getIdTurma());
                alerta.setTipo("atividade");
                alerta.setTitulo(titulo);
                alerta.setDescricao(aluno.getNome() + " ainda não tem atividade registada.");
                alerta.setDataAlerta(java.time.LocalDate.now());
                alerta.setLido(false);
                alertaRepository.save(alerta);
            }
        }
    }

    List<Alerta> alertas = (tipo == null || tipo.isBlank() || tipo.equalsIgnoreCase("todos"))
            ? alertaRepository.findByIdProfessorOrderByDataAlertaDescIdDesc(u.getIdUtilizador())
            : alertaRepository.findByIdProfessorAndTipoOrderByDataAlertaDescIdDesc(u.getIdUtilizador(), tipo);

    long totalNaoLidos = alertaRepository.countByIdProfessorAndLidoFalse(u.getIdUtilizador());
    long totalDesempenho = alertaRepository.countByIdProfessorAndTipoAndLidoFalse(u.getIdUtilizador(), "desempenho");
    long totalPendencia = alertaRepository.countByIdProfessorAndTipoAndLidoFalse(u.getIdUtilizador(), "pendencia");
    long totalAtividade = alertaRepository.countByIdProfessorAndTipoAndLidoFalse(u.getIdUtilizador(), "atividade");

    model.addAttribute("utilizador", u);
    model.addAttribute("alertas", alertas);
    model.addAttribute("tipoSelecionado", tipo == null ? "todos" : tipo);
    model.addAttribute("totalNaoLidos", totalNaoLidos);
    model.addAttribute("totalDesempenho", totalDesempenho);
    model.addAttribute("totalPendencia", totalPendencia);
    model.addAttribute("totalAtividade", totalAtividade);

    return "alertas";
}


@PostMapping("/alertas/lida/{id}")
public String marcarAlertaComoLida(@PathVariable Long id,
                                   HttpSession session) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("professor")) return "redirect:/login.html";

    Alerta alerta = alertaRepository.findById(id).orElse(null);
    if (alerta != null && alerta.getIdProfessor().equals(u.getIdUtilizador())) {
        alerta.setLido(true);
        alertaRepository.save(alerta);
    }

    return "redirect:/alertas";
}

@GetMapping("/conteudos-professor")
public String conteudosProfessor(@RequestParam(required = false) String tipo,
                                 HttpSession session,
                                 Model model) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("professor")) return "redirect:/login.html";

    List<ProfessorTurma> ligacoes = professorTurmaRepository.findByIdProfessor(u.getIdUtilizador());
    List<Long> idsTurmasProfessor = ligacoes.stream()
            .map(ProfessorTurma::getIdTurma)
            .toList();

    List<Turma> turmasProfessor = turmaRepository.findAllById(idsTurmasProfessor);

    String tipoSelecionado = (tipo == null || tipo.isBlank()) ? "videos" : tipo;

    List<Conteudo> conteudos = conteudoRepository.findAllByOrderByIdDesc();

    List<Map<String, Object>> conteudosView = conteudos.stream().map(c -> {
        Map<String, Object> item = new HashMap<>();
        item.put("id", c.getId());
        item.put("titulo", c.getTitulo());
        item.put("tema", c.getTema());
        item.put("descricao", c.getDescricao());
        item.put("duracao", c.getDuracao());
        item.put("videoUrl", c.getVideoUrl());
        item.put("temQuiz", quizRepository.existsByIdConteudo(String.valueOf(c.getId())));

        long totalTurmasAtribuidas = conteudoTurmaRepository.findByIdConteudo(c.getId()).size();
        item.put("totalTurmas", totalTurmasAtribuidas);

        return item;
    }).toList();

    List<Quiz> quizzes = quizRepository.findAll();

    List<Map<String, Object>> quizzesView = quizzes.stream().map(q -> {
        Map<String, Object> item = new HashMap<>();
        item.put("id", q.getId());
        item.put("titulo", q.getTitulo());
        item.put("tema", q.getTema());
        item.put("idConteudo", q.getIdConteudo());
        item.put("totalPerguntas", perguntaQuizRepository.countByIdQuiz(q.getId()));

        String tituloConteudo = conteudoRepository.findById(Long.valueOf(q.getIdConteudo()))
                .map(Conteudo::getTitulo)
                .orElse("Sem conteúdo");

        item.put("tituloConteudo", tituloConteudo);

        long totalTurmasAtribuidas = quizTurmaRepository.findByIdQuiz(q.getId()).size();
        item.put("totalTurmas", totalTurmasAtribuidas);

        return item;
    }).toList();

    long totalVideos = conteudos.size();
    long totalQuizzes = quizzes.size();

    model.addAttribute("utilizador", u);
    model.addAttribute("conteudos", conteudosView);
    model.addAttribute("quizzes", quizzesView);
    model.addAttribute("totalVideos", totalVideos);
    model.addAttribute("totalQuizzes", totalQuizzes);
    model.addAttribute("tipoSelecionado", tipoSelecionado);
    model.addAttribute("turmasProfessor", turmasProfessor);

    return "conteudos-professor";
}

@PostMapping("/conteudos-professor/atribuir-conteudo/{id}")
public String atribuirConteudoTurmas(@PathVariable Long id,
                                     @RequestParam(required = false, name = "turmas") List<Long> turmas,
                                     HttpSession session) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("professor")) return "redirect:/login.html";

    conteudoTurmaRepository.deleteByIdConteudo(id);

    if (turmas != null) {
        for (Long idTurma : turmas) {
            ConteudoTurma ct = new ConteudoTurma();
            ct.setIdConteudo(id);
            ct.setIdTurma(idTurma);
            conteudoTurmaRepository.save(ct);
        }
    }

    return "redirect:/conteudos-professor?tipo=videos";
}

@PostMapping("/conteudos-professor/atribuir-quiz/{id}")
public String atribuirQuizTurmas(@PathVariable Long id,
                                 @RequestParam(required = false, name = "turmas") List<Long> turmas,
                                 HttpSession session) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("professor")) return "redirect:/login.html";

    quizTurmaRepository.deleteByIdQuiz(id);

    if (turmas != null) {
        for (Long idTurma : turmas) {
            QuizTurma qt = new QuizTurma();
            qt.setIdQuiz(id);
            qt.setIdTurma(idTurma);
            quizTurmaRepository.save(qt);
        }
    }

    return "redirect:/conteudos-professor?tipo=quizzes";
}

@PostMapping("/conteudos-professor/apagar-quiz/{id}")
public String apagarQuiz(@PathVariable Long id,
                         HttpSession session) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("professor")) return "redirect:/login.html";

    if (quizRepository.existsById(id)) {
        perguntaQuizRepository.deleteByIdQuiz(id);
        quizRepository.deleteById(id);
    }

    return "redirect:/conteudos-professor?tipo=quizzes";
}

@GetMapping("/quiz-professor/{id}")
public String gerirPerguntasQuiz(@PathVariable Long id,
                                 HttpSession session,
                                 Model model) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("professor")) return "redirect:/login.html";

    Quiz quiz = quizRepository.findById(id).orElse(null);
    if (quiz == null) return "redirect:/conteudos-professor?tipo=quizzes";

    List<PerguntaQuiz> perguntas = perguntaQuizRepository.findByIdQuizOrderByOrdemAsc(id);

    List<Conteudo> todosConteudos = conteudoRepository.findAllByOrderByIdDesc();
    List<Conteudo> conteudosDisponiveis = todosConteudos.stream()
            .filter(c -> !quizRepository.existsByIdConteudo(String.valueOf(c.getId()))
                    || String.valueOf(c.getId()).equals(quiz.getIdConteudo()))
            .toList();

            String tituloConteudo = conteudoRepository.findById(Long.valueOf(quiz.getIdConteudo()))
        .map(Conteudo::getTitulo)
        .orElse("Sem conteúdo");

    model.addAttribute("utilizador", u);
    model.addAttribute("quiz", quiz);
    model.addAttribute("perguntas", perguntas);
    model.addAttribute("conteudos", conteudosDisponiveis);
    model.addAttribute("tituloConteudo", tituloConteudo);

    return "quiz-professor";
}

@PostMapping("/quiz-professor/{id}/editar")
public String editarQuizProfessor(@PathVariable Long id,
                                  @RequestParam String titulo,
                                  @RequestParam String tema,
                                  @RequestParam String idConteudo,
                                  HttpSession session) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("professor")) return "redirect:/login.html";

    Quiz quiz = quizRepository.findById(id).orElse(null);
    if (quiz == null) return "redirect:/conteudos-professor?tipo=quizzes";

    boolean conteudoJaUsado = quizRepository.existsByIdConteudo(idConteudo)
            && !idConteudo.equals(quiz.getIdConteudo());

    if (conteudoJaUsado) {
        return "redirect:/quiz-professor/" + id;
    }

    quiz.setTitulo(titulo);
    quiz.setTema(tema);
    quiz.setIdConteudo(idConteudo);

    quizRepository.save(quiz);

    return "redirect:/quiz-professor/" + id;
}


@PostMapping("/conteudos-professor/criar")
public String criarConteudo(@RequestParam String titulo,
                            @RequestParam String tema,
                            @RequestParam String descricao,
                            @RequestParam String duracao,
                            @RequestParam String videoUrl,
                            HttpSession session) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("professor")) return "redirect:/login.html";

    Conteudo conteudo = new Conteudo();
    conteudo.setTitulo(titulo);
    conteudo.setTema(tema);
    conteudo.setDescricao(descricao);
    conteudo.setDuracao(duracao);
    conteudo.setVideoUrl(videoUrl);

    conteudoRepository.save(conteudo);

    return "redirect:/conteudos-professor";
}


@PostMapping("/conteudos-professor/editar/{id}")
public String editarConteudo(@PathVariable Long id,
                             @RequestParam String titulo,
                             @RequestParam String tema,
                             @RequestParam String descricao,
                             @RequestParam String duracao,
                             @RequestParam String videoUrl,
                             HttpSession session) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("professor")) return "redirect:/login.html";

    Conteudo conteudo = conteudoRepository.findById(id).orElse(null);
    if (conteudo == null) return "redirect:/conteudos-professor";

    conteudo.setTitulo(titulo);
    conteudo.setTema(tema);
    conteudo.setDescricao(descricao);
    conteudo.setDuracao(duracao);
    conteudo.setVideoUrl(videoUrl);

    conteudoRepository.save(conteudo);

    return "redirect:/conteudos-professor";
}


@PostMapping("/conteudos-professor/apagar/{id}")
public String apagarConteudo(@PathVariable Long id,
                             HttpSession session) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("professor")) return "redirect:/login.html";

    if (conteudoRepository.existsById(id)) {
        conteudoRepository.deleteById(id);
    }

    return "redirect:/conteudos-professor";
}

@PostMapping("/quiz-professor/{idQuiz}/pergunta/criar")
public String criarPerguntaQuiz(@PathVariable Long idQuiz,
                                @RequestParam String enunciado,
                                @RequestParam String opcao1,
                                @RequestParam String opcao2,
                                @RequestParam String opcao3,
                                @RequestParam String opcao4,
                                @RequestParam String respostaCorreta,
                                HttpSession session) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("professor")) return "redirect:/login.html";

    List<PerguntaQuiz> perguntas = perguntaQuizRepository.findByIdQuizOrderByOrdemAsc(idQuiz);

    PerguntaQuiz pergunta = new PerguntaQuiz();
    pergunta.setIdQuiz(idQuiz);
    pergunta.setEnunciado(enunciado);
    pergunta.setOpcao1(opcao1);
    pergunta.setOpcao2(opcao2);
    pergunta.setOpcao3(opcao3);
    pergunta.setOpcao4(opcao4);
    pergunta.setRespostaCorreta(respostaCorreta);
    pergunta.setOrdem(perguntas.size() + 1);

    perguntaQuizRepository.save(pergunta);

    return "redirect:/quiz-professor/" + idQuiz;
}

@PostMapping("/quiz-professor/{idQuiz}/pergunta/editar/{idPergunta}")
public String editarPerguntaQuiz(@PathVariable Long idQuiz,
                                 @PathVariable Long idPergunta,
                                 @RequestParam String enunciado,
                                 @RequestParam String opcao1,
                                 @RequestParam String opcao2,
                                 @RequestParam String opcao3,
                                 @RequestParam String opcao4,
                                 @RequestParam String respostaCorreta,
                                 HttpSession session) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("professor")) return "redirect:/login.html";

    PerguntaQuiz pergunta = perguntaQuizRepository.findById(idPergunta).orElse(null);
    if (pergunta == null) return "redirect:/quiz-professor/" + idQuiz;

    pergunta.setEnunciado(enunciado);
    pergunta.setOpcao1(opcao1);
    pergunta.setOpcao2(opcao2);
    pergunta.setOpcao3(opcao3);
    pergunta.setOpcao4(opcao4);
    pergunta.setRespostaCorreta(respostaCorreta);

    perguntaQuizRepository.save(pergunta);

    return "redirect:/quiz-professor/" + idQuiz;
}

@PostMapping("/quiz-professor/{idQuiz}/pergunta/apagar/{idPergunta}")
public String apagarPerguntaQuiz(@PathVariable Long idQuiz,
                                 @PathVariable Long idPergunta,
                                 HttpSession session) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("professor")) return "redirect:/login.html";

    if (perguntaQuizRepository.existsById(idPergunta)) {
        perguntaQuizRepository.deleteById(idPergunta);
    }

    List<PerguntaQuiz> perguntas = perguntaQuizRepository.findByIdQuizOrderByOrdemAsc(idQuiz);
    int ordem = 1;
    for (PerguntaQuiz p : perguntas) {
        p.setOrdem(ordem++);
        perguntaQuizRepository.save(p);
    }

    return "redirect:/quiz-professor/" + idQuiz;
}

@GetMapping("/novo-quiz-professor")
public String novoQuizProfessor(HttpSession session, Model model) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("professor")) return "redirect:/login.html";

    List<Conteudo> todosConteudos = conteudoRepository.findAllByOrderByIdDesc();

    List<Conteudo> conteudosDisponiveis = todosConteudos.stream()
            .filter(c -> !quizRepository.existsByIdConteudo(String.valueOf(c.getId())))
            .toList();

    model.addAttribute("utilizador", u);
    model.addAttribute("conteudos", conteudosDisponiveis);

    return "novo-quiz-professor";
}

@PostMapping("/novo-quiz-professor")
public String guardarNovoQuizProfessor(
        @RequestParam String titulo,
        @RequestParam String tema,
        @RequestParam String idConteudo,

        @RequestParam String enunciado1,
        @RequestParam String opcao1_1,
        @RequestParam String opcao1_2,
        @RequestParam String opcao1_3,
        @RequestParam String opcao1_4,
        @RequestParam String respostaCorreta1,

        @RequestParam String enunciado2,
        @RequestParam String opcao2_1,
        @RequestParam String opcao2_2,
        @RequestParam String opcao2_3,
        @RequestParam String opcao2_4,
        @RequestParam String respostaCorreta2,

        @RequestParam String enunciado3,
        @RequestParam String opcao3_1,
        @RequestParam String opcao3_2,
        @RequestParam String opcao3_3,
        @RequestParam String opcao3_4,
        @RequestParam String respostaCorreta3,

        @RequestParam String enunciado4,
        @RequestParam String opcao4_1,
        @RequestParam String opcao4_2,
        @RequestParam String opcao4_3,
        @RequestParam String opcao4_4,
        @RequestParam String respostaCorreta4,

        @RequestParam String enunciado5,
        @RequestParam String opcao5_1,
        @RequestParam String opcao5_2,
        @RequestParam String opcao5_3,
        @RequestParam String opcao5_4,
        @RequestParam String respostaCorreta5,

        HttpSession session) {

    if (semLogin(session)) return "redirect:/login.html";

Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
if (!u.getTipo().equalsIgnoreCase("professor")) return "redirect:/login.html";

if (quizRepository.existsByIdConteudo(idConteudo)) {
    return "redirect:/conteudos-professor?tipo=quizzes";
}

Quiz quiz = new Quiz();
quiz.setTitulo(titulo);
quiz.setTema(tema);
quiz.setIdConteudo(idConteudo);
quizRepository.save(quiz);

    PerguntaQuiz p1 = new PerguntaQuiz();
    p1.setIdQuiz(quiz.getId());
    p1.setOrdem(1);
    p1.setEnunciado(enunciado1);
    p1.setOpcao1(opcao1_1);
    p1.setOpcao2(opcao1_2);
    p1.setOpcao3(opcao1_3);
    p1.setOpcao4(opcao1_4);
    p1.setRespostaCorreta(respostaCorreta1);
    perguntaQuizRepository.save(p1);

    PerguntaQuiz p2 = new PerguntaQuiz();
    p2.setIdQuiz(quiz.getId());
    p2.setOrdem(2);
    p2.setEnunciado(enunciado2);
    p2.setOpcao1(opcao2_1);
    p2.setOpcao2(opcao2_2);
    p2.setOpcao3(opcao2_3);
    p2.setOpcao4(opcao2_4);
    p2.setRespostaCorreta(respostaCorreta2);
    perguntaQuizRepository.save(p2);

    PerguntaQuiz p3 = new PerguntaQuiz();
    p3.setIdQuiz(quiz.getId());
    p3.setOrdem(3);
    p3.setEnunciado(enunciado3);
    p3.setOpcao1(opcao3_1);
    p3.setOpcao2(opcao3_2);
    p3.setOpcao3(opcao3_3);
    p3.setOpcao4(opcao3_4);
    p3.setRespostaCorreta(respostaCorreta3);
    perguntaQuizRepository.save(p3);

    PerguntaQuiz p4 = new PerguntaQuiz();
    p4.setIdQuiz(quiz.getId());
    p4.setOrdem(4);
    p4.setEnunciado(enunciado4);
    p4.setOpcao1(opcao4_1);
    p4.setOpcao2(opcao4_2);
    p4.setOpcao3(opcao4_3);
    p4.setOpcao4(opcao4_4);
    p4.setRespostaCorreta(respostaCorreta4);
    perguntaQuizRepository.save(p4);

    PerguntaQuiz p5 = new PerguntaQuiz();
    p5.setIdQuiz(quiz.getId());
    p5.setOrdem(5);
    p5.setEnunciado(enunciado5);
    p5.setOpcao1(opcao5_1);
    p5.setOpcao2(opcao5_2);
    p5.setOpcao3(opcao5_3);
    p5.setOpcao4(opcao5_4);
    p5.setRespostaCorreta(respostaCorreta5);
    perguntaQuizRepository.save(p5);

    return "redirect:/conteudos-professor?tipo=quizzes";
}

@PostMapping("/alertas/lidas")
public String marcarTodosComoLidos(HttpSession session) {
    if (semLogin(session)) return "redirect:/login.html";

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("professor")) return "redirect:/login.html";

    List<Alerta> alertas = alertaRepository.findByIdProfessorOrderByDataAlertaDescIdDesc(u.getIdUtilizador());

    for (Alerta alerta : alertas) {
        if (Boolean.FALSE.equals(alerta.getLido())) {
            alerta.setLido(true);
            alertaRepository.save(alerta);
        }
    }

    return "redirect:/alertas";
}

@GetMapping("/relatorios/dados")
@ResponseBody
public Map<String, Object> relatoriosDados(HttpSession session) {
    Map<String, Object> resposta = new HashMap<>();

    if (semLogin(session)) {
        resposta.put("erro", "Sem login");
        return resposta;
    }

    Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
    if (!u.getTipo().equalsIgnoreCase("professor")) {
        resposta.put("erro", "Acesso negado");
        return resposta;
    }

    List<ProfessorTurma> ligacoes = professorTurmaRepository.findByIdProfessor(u.getIdUtilizador());
    List<Long> idsTurmas = ligacoes.stream()
            .map(ProfessorTurma::getIdTurma)
            .toList();

    List<Turma> turmasProfessor = turmaRepository.findAllById(idsTurmas);

    List<Utilizador> alunos = idsTurmas.isEmpty()
            ? List.of()
            : utilizadorRepository.findByTipoAndIdTurmaInOrderByNomeAsc("aluno", idsTurmas);

    List<String> nomesTurmas = new ArrayList<>();
    List<Long> totalAlunosPorTurma = new ArrayList<>();
    List<Long> mediaPontosPorTurma = new ArrayList<>();

    for (Turma turma : turmasProfessor) {
        List<Utilizador> alunosTurma = utilizadorRepository.findByTipoAndIdTurmaOrderByNomeAsc("aluno", turma.getId());

        long total = alunosTurma.size();

        long media = Math.round(
                alunosTurma.stream()
                        .map(a -> a.getPontos() != null ? a.getPontos() : 0)
                        .mapToInt(Integer::intValue)
                        .average()
                        .orElse(0)
        );

        nomesTurmas.add(turma.getNome());
        totalAlunosPorTurma.add(total);
        mediaPontosPorTurma.add(media);
    }

    long totalAlunos = alunos.size();

    long mediaGlobal = Math.round(
            alunos.stream()
                    .map(a -> a.getPontos() != null ? a.getPontos() : 0)
                    .mapToInt(Integer::intValue)
                    .average()
                    .orElse(0)
    );

    String melhorTurma = "Sem dados";
    long melhorMedia = 0;

    for (int i = 0; i < turmasProfessor.size(); i++) {
        if (mediaPontosPorTurma.get(i) > melhorMedia) {
            melhorMedia = mediaPontosPorTurma.get(i);
            melhorTurma = turmasProfessor.get(i).getNome();
        }
    }

    List<String> meses = List.of("Jan", "Fev", "Mar", "Abr");
    List<Integer> evolucaoMedia = List.of(
            (int) Math.max(0, mediaGlobal - 18),
            (int) Math.max(0, mediaGlobal - 10),
            (int) Math.max(0, mediaGlobal - 5),
            (int) mediaGlobal
    );

    resposta.put("totalAlunos", totalAlunos);
    resposta.put("totalTurmas", turmasProfessor.size());
    resposta.put("mediaGlobal", mediaGlobal);
    resposta.put("melhorTurma", melhorTurma);
    resposta.put("melhorMedia", melhorMedia);
    resposta.put("nomesTurmas", nomesTurmas);
    resposta.put("totalAlunosPorTurma", totalAlunosPorTurma);
    resposta.put("mediaPontosPorTurma", mediaPontosPorTurma);
    resposta.put("meses", meses);
    resposta.put("evolucaoMedia", evolucaoMedia);

    return resposta;
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

    if ("professor".equalsIgnoreCase(u.getTipo())) {
        List<ProfessorTurma> ligacoes = professorTurmaRepository.findByIdProfessor(u.getIdUtilizador());
        List<Long> idsTurmas = ligacoes.stream()
                .map(ProfessorTurma::getIdTurma)
                .toList();

        long totalTurmas = idsTurmas.size();
        long totalAlunos = idsTurmas.isEmpty() ? 0 : utilizadorRepository.countByTipoAndIdTurmaIn("aluno", idsTurmas);

        model.addAttribute("totalTurmas", totalTurmas);
        model.addAttribute("totalAlunos", totalAlunos);
    }

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
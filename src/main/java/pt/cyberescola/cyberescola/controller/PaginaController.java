package pt.cyberescola.cyberescola.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import pt.cyberescola.cyberescola.model.EvolucaoPontuacao;
import pt.cyberescola.cyberescola.model.Utilizador;
import pt.cyberescola.cyberescola.repository.EvolucaoPontuacaoRepository;
import pt.cyberescola.cyberescola.repository.UtilizadorRepository;

@Controller
public class PaginaController {

    private final UtilizadorRepository utilizadorRepository;
    private final EvolucaoPontuacaoRepository evolucaoPontuacaoRepository;

    public PaginaController(UtilizadorRepository utilizadorRepository,
                            EvolucaoPontuacaoRepository evolucaoPontuacaoRepository) {
        this.utilizadorRepository = utilizadorRepository;
        this.evolucaoPontuacaoRepository = evolucaoPontuacaoRepository;
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

        model.addAttribute("utilizador", u);
        model.addAttribute("pontuacaoTotal", u.getPontos() != null ? u.getPontos() : 0);
        model.addAttribute("posicaoRanking", posicaoRanking);
        model.addAttribute("videosVistos", 0);
        model.addAttribute("quizzesFeitos", 0);
        model.addAttribute("semanasGrafico", semanas);
        model.addAttribute("pontosGrafico", pontosGrafico);

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
    public String conteudos(HttpSession session) {
        if (semLogin(session)) return "redirect:/login.html";
        return "conteudos";
    }

    @GetMapping("/quiz")
    public String quiz(HttpSession session) {
        if (semLogin(session)) return "redirect:/login.html";
        return "quiz";
    }

    @GetMapping("/ranking")
    public String ranking(HttpSession session) {
        if (semLogin(session)) return "redirect:/login.html";
        return "ranking";
    }

    @GetMapping("/gerir-turmas")
    public String gerirTurmas(HttpSession session) {
        if (semLogin(session)) return "redirect:/login.html";

        Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
        if (!u.getTipo().equalsIgnoreCase("professor")) return "redirect:/login.html";

        return "gerir-turmas";
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session) {
        if (semLogin(session)) return "redirect:/login.html";
        return "perfil";
    }
}
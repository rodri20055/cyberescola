package pt.cyberescola.cyberescola.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import pt.cyberescola.cyberescola.model.Utilizador;

@Controller
public class PaginaController {

    private boolean semLogin(HttpSession session) {
        return session.getAttribute("utilizadorLogado") == null;
    }

    @GetMapping("/aluno")
    public String aluno(HttpSession session) {
        if (semLogin(session)) return "redirect:/login.html";

        Utilizador u = (Utilizador) session.getAttribute("utilizadorLogado");
        if (!u.getTipo().equalsIgnoreCase("aluno")) return "redirect:/login.html";

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
}
package pt.cyberescola.cyberescola.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pt.cyberescola.cyberescola.model.Utilizador;
import pt.cyberescola.cyberescola.repository.UtilizadorRepository;

@Controller
public class RegistoController {

    private final UtilizadorRepository utilizadorRepository;

    public RegistoController(UtilizadorRepository utilizadorRepository) {
        this.utilizadorRepository = utilizadorRepository;
    }

    private boolean passwordSegura(String password) {
        if (password == null) return false;
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$");
    }

    @GetMapping("/criar-conta")
    public String paginaCriarConta() {
        return "criar-conta";
    }

    @PostMapping("/criar-conta")
    public String criarConta(@RequestParam String nome,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam String confirmarPassword,
                             @RequestParam String perfil) {

        if (!password.equals(confirmarPassword)) {
            return "redirect:/criar-conta?erro=password";
        }

        if (!passwordSegura(password)) {
            return "redirect:/criar-conta?erro=password-fraca";
        }

        Optional<Utilizador> existente = utilizadorRepository.findByEmail(email);

        if (existente.isPresent()) {
            return "redirect:/criar-conta?erro=email";
        }

        if (!perfil.equalsIgnoreCase("aluno")) {
            return "redirect:/criar-conta?erro=perfil";
        }

        Utilizador novo = new Utilizador();
        novo.setNome(nome);
        novo.setEmail(email);
        novo.setPalavraPasse(password);
        novo.setTipo("aluno");
        novo.setPontos(0);
        novo.setAtivo(true);
        novo.setTurma("Por atribuir");

        utilizadorRepository.save(novo);

        return "redirect:/login.html?contaCriada";
    }
}
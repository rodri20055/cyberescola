package pt.cyberescola.cyberescola.controller;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import pt.cyberescola.cyberescola.model.LoginCode;
import pt.cyberescola.cyberescola.model.Utilizador;
import pt.cyberescola.cyberescola.repository.LoginCodeRepository;
import pt.cyberescola.cyberescola.repository.UtilizadorRepository;
import pt.cyberescola.cyberescola.service.EmailService;

@Controller
public class LoginController {

    private final UtilizadorRepository utilizadorRepository;
    private final LoginCodeRepository loginCodeRepository;
    private final EmailService emailService;

    public LoginController(UtilizadorRepository utilizadorRepository,
                           LoginCodeRepository loginCodeRepository,
                           EmailService emailService) {
        this.utilizadorRepository = utilizadorRepository;
        this.loginCodeRepository = loginCodeRepository;
        this.emailService = emailService;
    }

    @PostMapping("/login")
public String login(@RequestParam String email,
                    @RequestParam String password,
                    @RequestParam String perfil,
                    HttpSession session) {

    Optional<Utilizador> user =
            utilizadorRepository.findByEmailAndPalavraPasse(email, password);

    if (user.isEmpty()) {
        return "redirect:/login.html?erro";
    }

    Utilizador u = user.get();

    if (!u.getTipo().equalsIgnoreCase(perfil)) {
        return "redirect:/login.html?erro";
    }

    if (u.getTwoFactorEnabled() != null && !u.getTwoFactorEnabled()) {
        session.setAttribute("utilizadorLogado", u);

        if (u.getTipo().equalsIgnoreCase("aluno")) {
            return "redirect:/aluno";
        } else if (u.getTipo().equalsIgnoreCase("professor")) {
            return "redirect:/professor";
        } else {
            return "redirect:/admin";
        }
    }

    String codigo = String.format("%06d", new Random().nextInt(1000000));

    LoginCode loginCode = new LoginCode();
    loginCode.setEmail(email);
    loginCode.setCodigo(codigo);
    loginCode.setExpiresAt(LocalDateTime.now().plusMinutes(5));
    loginCode.setUsed(false);
    loginCode.setTentativas(0);

    loginCodeRepository.save(loginCode);

    emailService.enviarEmail(
            email,
            "Código de verificação CyberEscola",
            "O teu código de verificação é: " + codigo
    );

    session.setAttribute("emailPendente2FA", email);
    session.setAttribute("perfilPendente2FA", perfil);

    return "redirect:/verificar-codigo";
}

    @GetMapping("/verificar-codigo")
    public String paginaVerificarCodigo(HttpSession session) {
        if (session.getAttribute("emailPendente2FA") == null) {
            return "redirect:/login.html";
        }
        return "verificar-codigo";
    }

    @PostMapping("/verificar-codigo")
    public String verificarCodigo(@RequestParam String codigo, HttpSession session) {

        String email = (String) session.getAttribute("emailPendente2FA");
        String perfil = (String) session.getAttribute("perfilPendente2FA");

        if (email == null || perfil == null) {
            return "redirect:/login.html";
        }

        Optional<LoginCode> loginCodeOpt =
                loginCodeRepository.findTopByEmailAndUsedFalseOrderByIdDesc(email);

        if (loginCodeOpt.isEmpty()) {
            return "redirect:/login.html?codigoInvalido";
        }

        LoginCode loginCode = loginCodeOpt.get();

        if (Boolean.TRUE.equals(loginCode.getUsed())) {
            return "redirect:/login.html?codigoUsado";
        }

        if (loginCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            return "redirect:/login.html?codigoExpirado";
        }

        if (loginCode.getTentativas() >= 3) {
            return "redirect:/login.html?bloqueado";
        }

        if (!loginCode.getCodigo().equals(codigo)) {
            loginCode.setTentativas(loginCode.getTentativas() + 1);
            loginCodeRepository.save(loginCode);
            return "redirect:/verificar-codigo?erro";
        }

        loginCode.setUsed(true);
        loginCodeRepository.save(loginCode);

        Optional<Utilizador> userOpt = utilizadorRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return "redirect:/login.html?erro";
        }

        Utilizador u = userOpt.get();

        session.removeAttribute("emailPendente2FA");
        session.removeAttribute("perfilPendente2FA");
        session.setAttribute("utilizadorLogado", u);

        if (u.getTipo().equalsIgnoreCase("aluno")) {
            return "redirect:/aluno";
        } else if (u.getTipo().equalsIgnoreCase("professor")) {
            return "redirect:/professor";
        } else {
            return "redirect:/admin";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login.html";
    }
}
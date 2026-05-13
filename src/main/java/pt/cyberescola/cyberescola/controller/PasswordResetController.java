package pt.cyberescola.cyberescola.controller;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pt.cyberescola.cyberescola.model.PasswordResetToken;
import pt.cyberescola.cyberescola.model.Utilizador;
import pt.cyberescola.cyberescola.repository.PasswordResetTokenRepository;
import pt.cyberescola.cyberescola.repository.UtilizadorRepository;
import pt.cyberescola.cyberescola.service.EmailService;

@Controller
public class PasswordResetController {

    private final UtilizadorRepository utilizadorRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;

    public PasswordResetController(UtilizadorRepository utilizadorRepository,
                                   PasswordResetTokenRepository tokenRepository,
                                   EmailService emailService) {
        this.utilizadorRepository = utilizadorRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    @GetMapping("/esqueci-password")
    public String paginaEsqueciPassword() {
        return "esqueci-password";
    }

  @PostMapping("/esqueci-password")
public String enviarLinkReset(@RequestParam String email) {

    System.out.println("ENTROU NO /esqueci-password");
    System.out.println("Email recebido: " + email);

    Optional<Utilizador> user = utilizadorRepository.findByEmail(email);

    System.out.println("Utilizador encontrado? " + user.isPresent());

    if (user.isPresent()) {
        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setEmail(email);
        resetToken.setToken(token);
        resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        resetToken.setUsed(false);

        tokenRepository.save(resetToken);

        String link = "https://cyberescola.onrender.com/nova-password?token=" + token;

        System.out.println("Token criado: " + token);
        System.out.println("Link gerado: " + link);
        System.out.println("A tentar enviar email para: " + email);

        emailService.enviarEmail(
                email,
                "Recuperação de palavra-passe",
                "Clica neste link:\n" + link
        );

        System.out.println("Chamou o EmailService");
    }

    return "redirect:/login.html?emailEnviado";
}

    @GetMapping("/nova-password")
    public String paginaNovaPassword(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "nova-password";
    }

    @PostMapping("/nova-password")
    public String guardarNovaPassword(@RequestParam String token,
                                     @RequestParam String password) {

        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);

        if (tokenOpt.isEmpty()) {
            return "redirect:/login.html?erroToken";
        }

        PasswordResetToken resetToken = tokenOpt.get();

        if (Boolean.TRUE.equals(resetToken.getUsed()) ||
            resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return "redirect:/login.html?tokenExpirado";
        }

        Optional<Utilizador> userOpt = utilizadorRepository.findByEmail(resetToken.getEmail());

        if (userOpt.isEmpty()) {
            return "redirect:/login.html?erro";
        }

        Utilizador user = userOpt.get();
        user.setPalavraPasse(password);
        utilizadorRepository.save(user);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        return "redirect:/login.html?passwordAlterada";
    }
}
package pt.cyberescola.cyberescola.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmail(String destino, String assunto, String mensagem) {
        try {
            System.out.println("ENTROU NO EmailService");
            System.out.println("Destino: " + destino);
            System.out.println("Assunto: " + assunto);
            System.out.println("Remetente: " + remetente);

            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(remetente);
            mail.setTo(destino);
            mail.setSubject(assunto);
            mail.setText(mensagem);

            mailSender.send(mail);

            System.out.println("EMAIL ENVIADO COM SUCESSO");
        } catch (Exception e) {
            System.out.println("ERRO AO ENVIAR EMAIL");
            e.printStackTrace();
        }
    }
}
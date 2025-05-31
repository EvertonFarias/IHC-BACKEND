package com.example.inovaTest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
        System.out.println("Email enviado com sucesso para " + to);
    }

    public String loadEmailTemplateVerification(String username, String verificationUrl) throws IOException {
        
        System.out.println("Carregando template de email de verificação...");
        ClassPathResource resource = new ClassPathResource("templates/email/email-verification.html");
        String htmlContent = new String(Files.readAllBytes(Path.of(resource.getURI())));
        System.out.println("Template carregado com sucesso.");

        // Substitui os placeholders
        return htmlContent.replace("${username}", username)
                         .replace("${verificationUrl}", verificationUrl);
    }
     public String loadResetPasswordTemplate(String username, String resetLink) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/email/reset-password.html");
        String htmlContent = new String(Files.readAllBytes(Path.of(resource.getURI())));

        return htmlContent.replace("${username}", username)
                         .replace("${resetLink}", resetLink)
                         .replace("${oceanBackgroundUrl}", "https://example.com/ocean-background.jpg");
    }
}
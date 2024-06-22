package com.dxc.solution_intelligente.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        emailSender.send(message);
    }

    public String getHtmlTemplate(String templatePath) throws IOException {
        Path path = Paths.get(templatePath);
        return new String(Files.readAllBytes(path));
    }

    public String populateTemplate(String template, String civility, String lastName, String firstName, String email, String password) {
        return template.replace("${civility}", civility)
                .replace("${lastName}", lastName.toUpperCase())
                .replace("${firstName}", firstName)
                .replace("${email}", email)
                .replace("${password}", password);
    }
}

package com.astridback.api.core.infrastructure.spring;

import com.astridback.api.core.application.ports.Mailer;
import com.astridback.api.core.application.services.TemplateService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.Map;

public class SpringMailer implements Mailer {
    private final JavaMailSender mailSender;
    private final TemplateService templateService;

    public SpringMailer(JavaMailSender mailSender, TemplateService templateService) {
        this.mailSender = mailSender;
        this.templateService = templateService;
    }

    private void send(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);
        } catch (MessagingException | MailException e) {
            throw new RuntimeException("Mail sent failed " + to, e);
        }
    }

    @Override
    public void sendVerificationCode(String to, String code) {
        final String body = templateService.render(
                "mail/verification-code.ftl",
                Map.of("code", code));

        send(to, "Verification code", body);
    }
}
package com.astridback.api.infrastructure.spring;

import com.astridback.api.application.ports.AuthContext;
import com.astridback.api.core.application.ports.Mailer;
import com.astridback.api.application.ports.UserRepository;
import com.astridback.api.infrastructure.persistence.jpa.SQLUserAccessor;
import com.astridback.api.infrastructure.persistence.jpa.SQLUserRepository;
import com.astridback.api.core.application.services.TemplateService;
import com.astridback.api.core.infrastructure.spring.SpringMailer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class AdaptersConfiguration {
    @Bean
    public UserRepository userRepository(SQLUserAccessor accessor) {
        return new SQLUserRepository(accessor);
    }

    @Bean
    public AuthContext getAuthContext() {
        return new SpringAuthContext();
    }

    @Bean
    public Mailer getMailer(JavaMailSender javaMailSender, TemplateService templateService) {
        return new SpringMailer(javaMailSender, templateService);
    }
}

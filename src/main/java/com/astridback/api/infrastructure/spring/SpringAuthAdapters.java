package com.astridback.api.infrastructure.spring;

import com.astridback.api.application.ports.AuthContext;
import com.astridback.api.core.application.ports.APILogger;
import com.astridback.api.core.application.services.TemplateService;
import com.astridback.api.core.infrastructure.services.TemplateServiceImpl;
import com.astridback.api.core.infrastructure.spring.SpringLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringAuthAdapters {
    @Bean
    APILogger apiLogger() {
        return new SpringLogger();
    }

    @Bean
    TemplateService templateService(freemarker.template.Configuration freemarker) {
        return new TemplateServiceImpl(freemarker);
    }

}

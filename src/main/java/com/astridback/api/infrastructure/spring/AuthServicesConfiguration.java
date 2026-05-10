package com.astridback.api.infrastructure.spring;

import com.astridback.api.application.services.JwtService;
import com.astridback.api.application.services.PasswordHasher;
import com.astridback.api.infrastructure.services.BcryptPasswordHasher;
import com.astridback.api.infrastructure.services.ConcreteJwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthServicesConfiguration {
    @Value("${jwt.access.secret}")
    private String jwtAccessSecret;

    @Value("${jwt.access.expiration}")
    private String jwtAccessExpiration;

    @Value("${jwt.refresh.secret}")
    private String jwtRefreshSecret;

    @Value("${jwt.refresh.expiration}")
    private String jwtRefreshExpiration;

    @Bean
    public PasswordHasher passwordHasher() {
        return new BcryptPasswordHasher();
    }

    @Bean
    public JwtService jwtService() {
        return new ConcreteJwtService(
                jwtAccessSecret,
                Long.parseLong(jwtAccessExpiration),
                jwtRefreshSecret,
                Long.parseLong(jwtRefreshExpiration));
    }
}

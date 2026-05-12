package com.astridback.api.infrastructure.spring;

import com.astridback.api.application.services.Base64Service;
import com.astridback.api.application.services.JwtService;
import com.astridback.api.application.services.PasswordHasher;
import com.astridback.api.domain.valueobject.PositionPayload;
import com.astridback.api.infrastructure.services.Base64ServiceImpl;
import com.astridback.api.infrastructure.services.BcryptPasswordHasher;
import com.astridback.api.infrastructure.services.ConcreteJwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class ServicesConfiguration {
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

    @Bean
    public Base64Service<PositionPayload> base64Service(ObjectMapper objectMapper) {
        return new Base64ServiceImpl<>(
                s -> {
                    try {
                        return objectMapper.readValue(s, PositionPayload.class);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}

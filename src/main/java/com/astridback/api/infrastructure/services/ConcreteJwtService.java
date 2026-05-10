package com.astridback.api.infrastructure.services;

import com.astridback.api.application.services.JwtService;
import com.astridback.api.domain.model.AuthUser;
import com.astridback.api.domain.model.User;
import com.astridback.api.domain.valueobject.Role;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.time.Instant;

public class ConcreteJwtService implements JwtService {

    private record TokenConfig(SecretKey key, JwtParser parser, long expirationMinutes) {
    }

    private final TokenConfig accessToken;
    private final TokenConfig refreshToken;

    public ConcreteJwtService(String secretAccess, long expirationAccess,
            String secretRefresh, long expirationRefresh) {
        this.accessToken = buildConfig(secretAccess, expirationAccess);
        this.refreshToken = buildConfig(secretRefresh, expirationRefresh);
    }

    private static TokenConfig buildConfig(String secret, long expirationMinutes) {
        final var key = Keys.hmacShaKeyFor(secret.getBytes());
        final var parser = Jwts.parser().verifyWith(key).build();
        return new TokenConfig(key, parser, expirationMinutes);
    }

    @Override
    public String generateAccessToken(User user) {
        return generateToken(user, accessToken);
    }

    @Override
    public String generateRefreshToken(User user) {
        return generateToken(user, refreshToken);
    }

    @Override
    public AuthUser parseAccessToken(String token) {
        return parseToken(accessToken.parser(), token);
    }

    @Override
    public AuthUser parseRefreshToken(String token) {
        return parseToken(refreshToken.parser(), token);
    }

    private String generateToken(User user, TokenConfig config) {
        final var now = Instant.now();
        final var claims = Jwts.claims()
                .subject(user.getId())
                .add("emailAddress", user.getEmailAddress())
                .add("role", user.getRole())
                .build();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(config.expirationMinutes() * 60)))
                .signWith(config.key())
                .compact();
    }

    private AuthUser parseToken(JwtParser parser, String token) {
        final var claims = parser.parseSignedClaims(token).getPayload();
        return new AuthUser(
                claims.getSubject(),
                claims.get("emailAddress", String.class),
                claims.get("role", String.class));
    }
}
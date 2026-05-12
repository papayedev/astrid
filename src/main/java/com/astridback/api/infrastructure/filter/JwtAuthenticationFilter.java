package com.astridback.api.infrastructure.filter;

import com.astridback.api.application.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private String apiKeyAstrocast;

    public JwtAuthenticationFilter(JwtService jwtService, @Value("${astrocast.secretKey}") String apiKeyAstrocast) {
        this.jwtService = jwtService;
        this.apiKeyAstrocast = apiKeyAstrocast;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            String astroKey = request.getHeader("X-Astrocast-Key");

            if (astroKey != null && astroKey.equals(apiKeyAstrocast)) {
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                "astrocast-service",
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_VISITOR"))
                        )
                );

                filterChain.doFilter(request, response);
                return;
            }

            final String header = request.getHeader("Authorization");

            if (header == null || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            final String jwt = header.substring(7);

            final var user = jwtService.parseAccessToken(jwt);

            if (user == null) {
                throw new BadCredentialsException("Invalid JWT token");
            }

            final var authentication = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_VISITOR")));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } finally {
            SecurityContextHolder.clearContext();
        }
    }
}
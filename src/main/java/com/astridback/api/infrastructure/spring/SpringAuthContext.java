package com.astridback.api.infrastructure.spring;

import com.astridback.api.application.ports.AuthContext;
import com.astridback.api.domain.model.AuthUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

public class SpringAuthContext implements AuthContext {
    @Override
    public boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    @Override
    public Optional<AuthUser> getAuthUser() {
        return Optional.ofNullable(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                .map(auth -> {
                    if (auth.getPrincipal() instanceof AuthUser) {
                        return (AuthUser) auth.getPrincipal();
                    }

                    return null;
                });
    }
}

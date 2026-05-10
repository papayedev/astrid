package com.astridback.api.application.ports;

import com.astridback.api.domain.model.AuthUser;

import java.util.Optional;

public interface AuthContext {
    boolean isAuthenticated();

    Optional<AuthUser> getAuthUser();
}

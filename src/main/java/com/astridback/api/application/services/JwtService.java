package com.astridback.api.application.services;

import com.astridback.api.domain.model.AuthUser;
import com.astridback.api.domain.model.User;

public interface JwtService {
    String generateAccessToken(User user);

    AuthUser parseAccessToken(String accessToken);

    String generateRefreshToken(User user);

    AuthUser parseRefreshToken(String refreshToken);
}

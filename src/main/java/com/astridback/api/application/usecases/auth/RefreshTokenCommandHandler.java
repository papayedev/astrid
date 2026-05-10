package com.astridback.api.application.usecases.auth;

import an.awesome.pipelinr.Command;
import com.astridback.api.domain.exceptions.NotFoundException;
import com.astridback.api.domain.exceptions.UnauthorizedException;
import com.astridback.api.application.ports.UserRepository;
import com.astridback.api.application.services.JwtService;
import com.astridback.api.domain.viewmodel.AccessTokenViewModel;
import com.astridback.api.core.application.ports.APILogger;

public class RefreshTokenCommandHandler implements Command.Handler<RefreshTokenCommand, AccessTokenViewModel> {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final APILogger apiLogger;

    public RefreshTokenCommandHandler(JwtService jwtService, UserRepository userRepository, APILogger apiLogger) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.apiLogger = apiLogger;
    }

    @Override
    public AccessTokenViewModel handle(RefreshTokenCommand refreshTokenCommand) {
        try {
            apiLogger.info("RefreshCommand received");
            var authUser = jwtService.parseRefreshToken(refreshTokenCommand.refreshToken());

            var user = userRepository.findById(authUser.getId())
                    .orElseThrow(NotFoundException::new);

            var accessToken = jwtService.generateAccessToken(user);

            apiLogger.info("Refresh token generated");

            return new AccessTokenViewModel(accessToken);
        } catch (IllegalArgumentException e) {
            apiLogger.error("Invalid refresh token");
            throw new UnauthorizedException();
        }
    }
}

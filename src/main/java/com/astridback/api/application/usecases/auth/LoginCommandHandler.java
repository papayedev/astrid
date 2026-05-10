package com.astridback.api.application.usecases.auth;

import an.awesome.pipelinr.Command;
import com.astridback.api.domain.exceptions.UnauthorizedException;
import com.astridback.api.application.ports.UserRepository;
import com.astridback.api.application.services.JwtService;
import com.astridback.api.application.services.PasswordHasher;
import com.astridback.api.domain.viewmodel.LoggedInUserViewModel;
import com.astridback.api.core.application.ports.APILogger;
import com.astridback.api.domain.exceptions.ForbiddenException;
import com.astridback.api.domain.exceptions.NotFoundException;

public class LoginCommandHandler implements Command.Handler<LoginCommand, LoggedInUserViewModel> {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordHasher passwordHasher;
    private final APILogger apiLogger;

    public LoginCommandHandler(UserRepository userRepository,
            JwtService jwtService,
            PasswordHasher passwordHasher,
            APILogger apiLogger) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordHasher = passwordHasher;
        this.apiLogger = apiLogger;
    }

    @Override
    public LoggedInUserViewModel handle(LoginCommand loginCommand) {
        apiLogger.info("Login command received");
        var user = this.userRepository
                .findByEmailAddress(loginCommand.emailAddress())
                .orElseThrow(() -> new NotFoundException("User"));

        var match = this.passwordHasher.match(
                loginCommand.password(),
                user.getPassword());

        if (!match) {
            throw new UnauthorizedException();
        }

        if (!user.isActive()) {
            throw new ForbiddenException("User is not active");
        }

        var accessToken = this.jwtService.generateAccessToken(user);
        var refreshToken = this.jwtService.generateRefreshToken(user);

        apiLogger.info("Refresh and access token generated");

        return new LoggedInUserViewModel(user.getId(), user.getEmailAddress(), accessToken, refreshToken, user.getRole().toString());
    }
}
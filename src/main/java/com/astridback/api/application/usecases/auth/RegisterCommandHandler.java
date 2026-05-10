package com.astridback.api.application.usecases.auth;

import an.awesome.pipelinr.Command;
import com.astridback.api.domain.exceptions.UnauthorizedException;
import com.astridback.api.core.application.ports.Mailer;
import com.astridback.api.application.ports.UserRepository;
import com.astridback.api.application.services.PasswordHasher;
import com.astridback.api.domain.model.User;
import com.astridback.api.domain.viewmodel.IdResponse;
import com.astridback.api.core.application.ports.APILogger;

import java.util.UUID;

public class RegisterCommandHandler implements Command.Handler<RegisterCommand, IdResponse> {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final Mailer mailer;
    private final APILogger apiLogger;

    public RegisterCommandHandler(UserRepository userRepository,
            PasswordHasher passwordHasher,
            Mailer mailer,
            APILogger apiLogger) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.mailer = mailer;
        this.apiLogger = apiLogger;
    }

    private User generateUser(String email, String password) {
        var user = new User(
                UUID.randomUUID().toString(),
                email,
                password, null);
        user.resetPassword(passwordHasher.hash(password));
        user.createVerificationCode(15);
        userRepository.save(user);
        return user;
    }

    private void sendVerificationCode(User user) {
        var verificationCode = user.getVerificationCode();
        mailer.sendVerificationCode(user.getEmailAddress(), verificationCode);
    }

    @Override
    public IdResponse handle(RegisterCommand registerCommand) {
        apiLogger.info("Register command received");
        var maybeUser = userRepository.findByEmailAddress(registerCommand.emailAddress());
        if (maybeUser.isPresent()) {
            throw new UnauthorizedException();
        }
        var user = generateUser(registerCommand.emailAddress(), registerCommand.password());
        sendVerificationCode(user);
        apiLogger.info("New verification code generated");
        return new IdResponse(user.getId());
    }
}
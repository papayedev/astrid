package com.astridback.api.application.usecases.auth;

import an.awesome.pipelinr.Command;
import com.astridback.api.domain.exceptions.UnauthorizedException;
import com.astridback.api.application.ports.UserRepository;
import com.astridback.api.application.services.PasswordHasher;
import com.astridback.api.domain.valueobject.Password;
import com.astridback.api.domain.viewmodel.VoidResponse;
import com.astridback.api.core.application.ports.APILogger;
import com.astridback.api.domain.exceptions.ForbiddenException;
import com.astridback.api.domain.exceptions.NotFoundException;

import java.util.Objects;

public class UpdatePasswordCommandHandler implements Command.Handler<UpdatePasswordCommand, VoidResponse> {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final APILogger apiLogger;

    public UpdatePasswordCommandHandler(UserRepository userRepository, PasswordHasher passwordHasher,
            APILogger apiLogger) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.apiLogger = apiLogger;
    }

    @Override
    public VoidResponse handle(UpdatePasswordCommand updatePasswordCommand) {
        apiLogger.info("Update Password Request");

        final var user = userRepository.findByEmailAddress(updatePasswordCommand.email())
                .orElseThrow(() -> new NotFoundException("User"));

        if (!user.isActive()) {
            throw new ForbiddenException("User is not active");
        }

        if (user.getVerificationCode() == null) {
            throw new UnauthorizedException();
        }

        if (user.isVerificationCodeExpired()) {
            throw new UnauthorizedException();
        }

        var password = new Password(updatePasswordCommand.password(), null);

        if (Objects.equals(user.getVerificationCode(), updatePasswordCommand.verificationCode())) {
            user.resetPassword(passwordHasher.hash(password.getClearPassword()));
            userRepository.save(user);
            apiLogger.info("Password updated");
        } else {
            throw new UnauthorizedException();
        }

        return new VoidResponse();
    }
}

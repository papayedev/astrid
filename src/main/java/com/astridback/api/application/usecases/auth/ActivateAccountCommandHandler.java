package com.astridback.api.application.usecases.auth;

import an.awesome.pipelinr.Command;
import com.astridback.api.application.ports.UserRepository;
import com.astridback.api.domain.viewmodel.IdResponse;
import com.astridback.api.core.application.ports.APILogger;
import com.astridback.api.domain.exceptions.BadRequestException;
import com.astridback.api.domain.exceptions.ForbiddenException;
import com.astridback.api.domain.exceptions.NotFoundException;

public class ActivateAccountCommandHandler implements Command.Handler<ActivateAccountCommand, IdResponse> {
    private final UserRepository userRepository;
    private final APILogger apiLogger;

    public ActivateAccountCommandHandler(UserRepository userRepository, APILogger apiLogger) {
        this.userRepository = userRepository;
        this.apiLogger = apiLogger;
    }

    @Override
    public IdResponse handle(ActivateAccountCommand command) {
        apiLogger.info("Active account command received");
        var user = userRepository.findByEmailAddress(command.email())
                .orElseThrow(() -> new NotFoundException("User"));

        if (user.isActive()) {
            throw new ForbiddenException("User is already active");
        }

        var match = command.verificationCode().equals(user.getVerificationCode());

        if (!match) {
            throw new BadRequestException("Verification code is wrong");
        }

        user.makeActive();
        user.resetVerificationCode();

        userRepository.save(user);

        apiLogger.info("Active account has been activated");

        return new IdResponse(user.getId());
    }
}

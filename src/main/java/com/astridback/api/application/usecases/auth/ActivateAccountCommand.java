package com.astridback.api.application.usecases.auth;

import an.awesome.pipelinr.Command;
import com.astridback.api.domain.viewmodel.IdResponse;

public record ActivateAccountCommand(
        String email,
        String verificationCode
) implements Command<IdResponse> {

}

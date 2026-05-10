package com.astridback.api.application.usecases.auth;

import an.awesome.pipelinr.Command;
import com.astridback.api.domain.viewmodel.VoidResponse;

public record UpdatePasswordCommand(
        String email,
        String verificationCode,
        String password
) implements Command<VoidResponse> {

}

package com.astridback.api.application.usecases.auth;

import an.awesome.pipelinr.Command;
import com.astridback.api.domain.viewmodel.IdResponse;

public record RegisterCommand(
        String emailAddress,
        String password
) implements Command<IdResponse> {

}

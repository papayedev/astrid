package com.astridback.api.application.usecases.auth;

import an.awesome.pipelinr.Command;
import com.astridback.api.domain.viewmodel.AccessTokenViewModel;

public record RefreshTokenCommand(
        String refreshToken
) implements Command<AccessTokenViewModel> {

}

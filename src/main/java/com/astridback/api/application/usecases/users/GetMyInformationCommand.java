package com.astridback.api.application.usecases.users;

import an.awesome.pipelinr.Command;
import com.astridback.api.domain.viewmodel.UserDetailsViewModel;

public record GetMyInformationCommand(
        String userId
) implements Command<UserDetailsViewModel> {

}

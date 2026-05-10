package com.astridback.api.application.usecases.users;

import an.awesome.pipelinr.Command;
import com.astridback.api.application.ports.UserRepository;
import com.astridback.api.domain.exceptions.UnauthorizedException;
import com.astridback.api.domain.viewmodel.UserDetailsViewModel;

public class GetMyInformationCommandHandler implements Command.Handler<GetMyInformationCommand, UserDetailsViewModel> {
    private final UserRepository userRepository;

    public GetMyInformationCommandHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetailsViewModel handle(GetMyInformationCommand command) {
        var user = userRepository.findById(command.userId())
                .orElseThrow(UnauthorizedException::new);

        return new UserDetailsViewModel(
                user.getEmailAddress(),
                user.getRole().toString());
    }
}

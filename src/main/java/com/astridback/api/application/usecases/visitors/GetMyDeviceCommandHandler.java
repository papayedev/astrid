package com.astridback.api.application.usecases.visitors;

import an.awesome.pipelinr.Command;
import com.astridback.api.application.ports.UserRepository;
import com.astridback.api.domain.exceptions.NotFoundException;
import com.astridback.api.domain.exceptions.UnauthorizedException;
import com.astridback.api.domain.viewmodel.DeviceResponse;

public class GetMyDeviceCommandHandler implements Command.Handler<GetMyDeviceCommand, DeviceResponse> {
    private final UserRepository userRepository;

    public GetMyDeviceCommandHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public DeviceResponse handle(GetMyDeviceCommand command) {
        var user = userRepository.findById(command.userId())
                .orElseThrow(UnauthorizedException::new);

        if (!user.hasAlreadyDevice()) {
            throw new NotFoundException("Device");
        }

        return new DeviceResponse(
                user.getDevice().getId(),
                user.getDevice().getName(),
                user.getDevice().getSerialNumber().getValue(),
                user.getDevice().getSerialPin().getValue(),
                command.userId()
        );
    }
}

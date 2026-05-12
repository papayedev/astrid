package com.astridback.api.application.usecases.admin;

import an.awesome.pipelinr.Command;
import com.astridback.api.application.ports.DeviceRepository;
import com.astridback.api.domain.exceptions.NotFoundException;
import com.astridback.api.domain.exceptions.UnauthorizedException;
import com.astridback.api.domain.valueobject.Role;
import com.astridback.api.domain.viewmodel.VoidResponse;

public class DeleteDeviceCommandHandler implements Command.Handler<DeleteDeviceCommand, VoidResponse> {
    private final DeviceRepository deviceRepository;

    public DeleteDeviceCommandHandler(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public VoidResponse handle(DeleteDeviceCommand command) {
        if (!command.role().equals(Role.ADMIN)) {
            throw new UnauthorizedException();
        }

        deviceRepository.findById(command.id())
                        .orElseThrow(NotFoundException::new);

        deviceRepository.deleteById(command.id());

        return new VoidResponse();
    }
}

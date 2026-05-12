package com.astridback.api.application.usecases.admin;

import an.awesome.pipelinr.Command;
import com.astridback.api.application.ports.DeviceRepository;
import com.astridback.api.domain.exceptions.ForbiddenException;
import com.astridback.api.domain.exceptions.UnauthorizedException;
import com.astridback.api.domain.model.Device;
import com.astridback.api.domain.valueobject.Pin;
import com.astridback.api.domain.valueobject.Role;
import com.astridback.api.domain.valueobject.SerialNumber;
import com.astridback.api.domain.viewmodel.IdResponse;

import java.util.UUID;

public class CreateDeviceCommandHandler implements Command.Handler<CreateDeviceCommand, IdResponse> {
    private final DeviceRepository deviceRepository;

    public CreateDeviceCommandHandler(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public IdResponse handle(CreateDeviceCommand command) {
        if (!command.role().equals(Role.ADMIN)) {
            throw new UnauthorizedException();
        }

        var maybeDeviceByName = deviceRepository.findByName(command.name());
        var maybeDeviceBySerialNumber = deviceRepository.findBySerialNumber(command.serialNumber());

        if (maybeDeviceByName.isPresent()) {
            throw new ForbiddenException("Name is already use");
        }

        if (maybeDeviceBySerialNumber.isPresent()) {
            throw new ForbiddenException("Serial number is already use");
        }

        var id = UUID.randomUUID().toString();
        var device = new Device(
                id,
                new SerialNumber(command.serialNumber()),
                new Pin(command.pin()),
                null,
                command.name()
        );

        deviceRepository.save(device);

        return new IdResponse(id);
    }
}

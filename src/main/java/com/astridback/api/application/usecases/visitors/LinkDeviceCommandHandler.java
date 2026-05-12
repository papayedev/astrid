package com.astridback.api.application.usecases.visitors;

import an.awesome.pipelinr.Command;
import com.astridback.api.application.ports.DeviceRepository;
import com.astridback.api.application.ports.UserRepository;
import com.astridback.api.domain.exceptions.NotFoundException;
import com.astridback.api.domain.exceptions.UnauthorizedException;
import com.astridback.api.domain.viewmodel.VoidResponse;

public class LinkDeviceCommandHandler implements Command.Handler<LinkDeviceCommand, VoidResponse> {
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;

    public LinkDeviceCommandHandler(UserRepository userRepository, DeviceRepository deviceRepository) {
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public VoidResponse handle(LinkDeviceCommand command) {
        var user = userRepository.findById(command.userId())
                .orElseThrow(UnauthorizedException::new);

        if (user.hasAlreadyDevice()) {
            throw new UnauthorizedException("You have already device");
        }

        var maybeDevice = deviceRepository.findBySerialNumber(command.serialNumber());

        if (maybeDevice.isEmpty()) {
            throw new NotFoundException("Serial Number");
        }

        var device = maybeDevice.get();

        if (device.hasUser()) {
            throw new UnauthorizedException();
        }

        if (!device.matchPin(command.pin())) {
            throw new UnauthorizedException("Pin wrong");
        }

        device.updateName(command.name());

        user.linkDevice(device);
        userRepository.save(user);

        return new VoidResponse();
    }
}

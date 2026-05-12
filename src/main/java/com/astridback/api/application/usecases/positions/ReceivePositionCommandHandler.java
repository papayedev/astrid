package com.astridback.api.application.usecases.positions;

import an.awesome.pipelinr.Command;
import com.astridback.api.application.ports.DeviceRepository;
import com.astridback.api.application.services.Base64Service;
import com.astridback.api.domain.exceptions.NotFoundException;
import com.astridback.api.domain.exceptions.UnauthorizedException;
import com.astridback.api.domain.model.Position;
import com.astridback.api.domain.valueobject.PositionPayload;
import com.astridback.api.domain.viewmodel.VoidResponse;

import java.util.UUID;

public class ReceivePositionCommandHandler implements Command.Handler<ReceivePositionCommand, VoidResponse> {
    private final DeviceRepository deviceRepository;
    private final Base64Service<PositionPayload> base64Service;

    public ReceivePositionCommandHandler(DeviceRepository deviceRepository,  Base64Service<PositionPayload> base64Service) {
        this.deviceRepository = deviceRepository;
        this.base64Service = base64Service;
    }

    @Override
    public VoidResponse handle(ReceivePositionCommand command) {
        var position = base64Service.decode(command.payload())
                .orElseThrow(UnauthorizedException::new);

        if (position.serialNumber() == null
                || position.latitude() == null
                || position.longitude() == null) {
            throw new UnauthorizedException();
        }

        var device = deviceRepository.findBySerialNumber(position.serialNumber())
                .orElseThrow(NotFoundException::new);

        device.addPosition(
                UUID.randomUUID().toString(),
                position.latitude(),
                position.longitude()
        );

        deviceRepository.save(device);

        return new VoidResponse();
    }
}

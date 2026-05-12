package com.astridback.api.application.usecases.admin;

import an.awesome.pipelinr.Command;
import com.astridback.api.application.ports.DeviceRepository;
import com.astridback.api.domain.exceptions.NotFoundException;
import com.astridback.api.domain.exceptions.UnauthorizedException;
import com.astridback.api.domain.valueobject.Role;
import com.astridback.api.domain.viewmodel.AllDeviceResponse;
import com.astridback.api.domain.viewmodel.DeviceResponse;

import java.util.Objects;

public class GetAllDevicesCommandHandler implements Command.Handler<GetAllDevicesCommand, AllDeviceResponse> {
    private final DeviceRepository deviceRepository;

    public GetAllDevicesCommandHandler(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public AllDeviceResponse handle(GetAllDevicesCommand command) {
        if (!command.role().equals(Role.ADMIN)) {
            throw new UnauthorizedException();
        }
        var devices = deviceRepository.findAll(command.pageNumber())
                .orElseThrow(NotFoundException::new);
        var response = devices.getDevices()
                    .stream()
                    .map(data -> new DeviceResponse(
                            data.getId(),
                            data.getName(),
                            data.getSerialNumber().getValue(),
                            data.getSerialPin().getValue(),
                            data.getUser() != null ? data.getUser().getId() : "0"))
                    .toList();
        return new AllDeviceResponse(response, devices.getMaxPage());
    }
}

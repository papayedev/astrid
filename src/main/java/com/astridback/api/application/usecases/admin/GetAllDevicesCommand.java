package com.astridback.api.application.usecases.admin;

import an.awesome.pipelinr.Command;
import com.astridback.api.domain.valueobject.Role;
import com.astridback.api.domain.viewmodel.AllDeviceResponse;

public record GetAllDevicesCommand(
        Role role,
        int pageNumber
) implements Command<AllDeviceResponse> {

}

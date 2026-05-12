package com.astridback.api.application.usecases.admin;

import an.awesome.pipelinr.Command;
import com.astridback.api.domain.valueobject.Role;
import com.astridback.api.domain.viewmodel.VoidResponse;

public record DeleteDeviceCommand(
        String id,
        Role role
) implements Command<VoidResponse> {

}

package com.astridback.api.application.usecases.admin;

import an.awesome.pipelinr.Command;
import com.astridback.api.domain.valueobject.Role;
import com.astridback.api.domain.viewmodel.IdResponse;

public record CreateDeviceCommand(
    String name,
    Role role,
    String serialNumber,
    String pin
) implements Command<IdResponse> {}

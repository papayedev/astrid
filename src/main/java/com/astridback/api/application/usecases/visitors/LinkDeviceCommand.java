package com.astridback.api.application.usecases.visitors;

import an.awesome.pipelinr.Command;
import com.astridback.api.domain.viewmodel.VoidResponse;

public record LinkDeviceCommand(
        String userId,
        String name,
        String serialNumber,
        String pin
) implements Command<VoidResponse> {

}

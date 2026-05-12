package com.astridback.api.application.usecases.visitors;

import an.awesome.pipelinr.Command;
import com.astridback.api.domain.viewmodel.DeviceResponse;

public record GetMyDeviceCommand (
         String userId
) implements Command<DeviceResponse> {
}

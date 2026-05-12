package com.astridback.api.application.usecases.positions;

import an.awesome.pipelinr.Command;
import com.astridback.api.domain.viewmodel.VoidResponse;

public record ReceivePositionCommand(
    String payload
) implements Command<VoidResponse> {}
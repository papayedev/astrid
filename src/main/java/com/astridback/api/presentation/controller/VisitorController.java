package com.astridback.api.presentation.controller;

import an.awesome.pipelinr.Pipeline;
import com.astridback.api.application.ports.AuthContext;
import com.astridback.api.application.usecases.visitors.GetMyDeviceCommand;
import com.astridback.api.application.usecases.visitors.LinkDeviceCommand;
import com.astridback.api.domain.exceptions.UnauthorizedException;
import com.astridback.api.domain.viewmodel.DeviceResponse;
import com.astridback.api.domain.viewmodel.VoidResponse;
import com.astridback.api.presentation.dto.LinkDeviceDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/visitors")
public class VisitorController {
    private final AuthContext authContext;
    private final Pipeline pipeline;

    public VisitorController(AuthContext authContext, Pipeline pipeline) {
        this.authContext = authContext;
        this.pipeline = pipeline;
    }

    @PostMapping("/devices/link")
    public ResponseEntity<VoidResponse> link(
            @RequestBody LinkDeviceDTO dto
            ) {
        var user = authContext.getAuthUser().orElseThrow(UnauthorizedException::new);
        var command = new LinkDeviceCommand(
            user.getId(),
            dto.name(),
            dto.serialNumber(),
            dto.pin()
        );
        return ResponseEntity.ok(this.pipeline.send(command));
    }

    @GetMapping("/devices")
    public ResponseEntity<DeviceResponse> findDevice() {
        var user = authContext.getAuthUser().orElseThrow(UnauthorizedException::new);
        var command = new GetMyDeviceCommand(user.getId());
        return ResponseEntity.ok(this.pipeline.send(command));
    }
}

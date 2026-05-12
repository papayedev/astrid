package com.astridback.api.presentation.controller;

import an.awesome.pipelinr.Pipeline;
import com.astridback.api.application.ports.AuthContext;
import com.astridback.api.application.usecases.admin.CreateDeviceCommand;
import com.astridback.api.application.usecases.admin.DeleteDeviceCommand;
import com.astridback.api.application.usecases.admin.GetAllDevicesCommand;
import com.astridback.api.domain.exceptions.UnauthorizedException;
import com.astridback.api.domain.valueobject.Role;
import com.astridback.api.domain.viewmodel.AllDeviceResponse;
import com.astridback.api.domain.viewmodel.IdResponse;
import com.astridback.api.domain.viewmodel.VoidResponse;
import com.astridback.api.presentation.dto.CreateDeviceDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final Pipeline pipeline;
    private final AuthContext authContext;

    public AdminController(Pipeline pipeline, AuthContext authContext) {
        this.pipeline = pipeline;
        this.authContext = authContext;
    }

    @GetMapping("/devices")
    public ResponseEntity<AllDeviceResponse> getAll(
            @RequestParam(defaultValue = "0") int page
    ) {
        var user = authContext.getAuthUser().orElseThrow(UnauthorizedException::new);
        var command = new GetAllDevicesCommand(user.getRole(), page);
        return ResponseEntity.ok(this.pipeline.send(command));
    }

    @DeleteMapping("/devices/{id}")
    public ResponseEntity<VoidResponse> delete(@PathVariable String id) {
        var user = authContext.getAuthUser().orElseThrow(UnauthorizedException::new);
        var command = new DeleteDeviceCommand(id, user.getRole());
        return ResponseEntity.ok(this.pipeline.send(command));
    }

    @PostMapping("/devices")
    public ResponseEntity<IdResponse> create(
            @RequestBody CreateDeviceDTO dto
    ) {
        var user = authContext.getAuthUser().orElseThrow(UnauthorizedException::new);
        var result = this.pipeline.send(new CreateDeviceCommand(
                dto.name(),
                user.getRole(),
                dto.serialNumber(),
                dto.pin()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}

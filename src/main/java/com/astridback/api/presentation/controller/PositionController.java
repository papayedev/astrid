package com.astridback.api.presentation.controller;

import an.awesome.pipelinr.Pipeline;
import com.astridback.api.application.usecases.positions.ReceivePositionCommand;
import com.astridback.api.domain.viewmodel.VoidResponse;
import com.astridback.api.presentation.dto.ReceivePositionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/positions")
public class PositionController {
    private final Pipeline pipeline;

    public PositionController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping
    public ResponseEntity<VoidResponse> receive(@RequestBody ReceivePositionDTO dto) {
        return ResponseEntity.ok(
                pipeline.send(new ReceivePositionCommand(dto.payload()))
        );
    }
}

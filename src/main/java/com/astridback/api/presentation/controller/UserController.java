package com.astridback.api.presentation.controller;

import an.awesome.pipelinr.Pipeline;
import com.astridback.api.application.ports.AuthContext;
import com.astridback.api.application.usecases.users.GetMyInformationCommand;
import com.astridback.api.domain.exceptions.UnauthorizedException;
import com.astridback.api.domain.viewmodel.LoggedInUserViewModel;
import com.astridback.api.domain.viewmodel.UserDetailsViewModel;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Transactional
public class UserController {
    private final AuthContext authContext;
    private final Pipeline pipeline;

    public UserController(AuthContext authContext, Pipeline pipeline) {
        this.authContext = authContext;
        this.pipeline = pipeline;
    }

    @GetMapping()
    public ResponseEntity<UserDetailsViewModel> me() {
        var user = authContext.getAuthUser()
                .orElseThrow(UnauthorizedException::new);

        return ResponseEntity.ok(this.pipeline.send(new GetMyInformationCommand(user.getId())));
    }
}

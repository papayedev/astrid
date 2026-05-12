package com.astridback.api.infrastructure.spring;

import com.astridback.api.application.ports.DeviceRepository;
import com.astridback.api.application.services.Base64Service;
import com.astridback.api.application.usecases.positions.ReceivePositionCommandHandler;
import com.astridback.api.domain.valueobject.PositionPayload;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PositionUseCasesConfiguration {
    @Bean
    public ReceivePositionCommandHandler receivePositionCommandHandler(DeviceRepository
                                                                        deviceRepository,
                                                                       Base64Service<PositionPayload> base64Service) {
        return new ReceivePositionCommandHandler(deviceRepository, base64Service);
    }
}

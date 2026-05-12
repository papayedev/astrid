package com.astridback.api.infrastructure.spring;

import com.astridback.api.application.ports.DeviceRepository;
import com.astridback.api.application.ports.UserRepository;
import com.astridback.api.application.usecases.visitors.GetMyDeviceCommandHandler;
import com.astridback.api.application.usecases.visitors.LinkDeviceCommandHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VisitorUseCasesConfiguration {
    @Bean
    public LinkDeviceCommandHandler linkDeviceCommandHandler(UserRepository userRepository, DeviceRepository deviceRepository) {
        return new LinkDeviceCommandHandler(userRepository, deviceRepository);
    }

    @Bean
    public GetMyDeviceCommandHandler getMyDeviceCommandHandler(UserRepository userRepository) {
        return new GetMyDeviceCommandHandler(userRepository);
    }
}

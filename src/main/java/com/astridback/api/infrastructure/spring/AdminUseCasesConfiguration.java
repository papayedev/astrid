package com.astridback.api.infrastructure.spring;

import com.astridback.api.application.ports.DeviceRepository;
import com.astridback.api.application.ports.UserRepository;
import com.astridback.api.application.usecases.admin.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminUseCasesConfiguration {
    @Bean
    public CreateDeviceCommandHandler  createDeviceCommandHandler(UserRepository userRepository, DeviceRepository deviceRepository) {
        return new CreateDeviceCommandHandler(deviceRepository);
    }

    @Bean
    public GetAllDevicesCommandHandler getAllDevicesCommand(DeviceRepository deviceRepository) {
        return new GetAllDevicesCommandHandler(deviceRepository);
    }

    @Bean
    public DeleteDeviceCommandHandler deleteDeviceCommandHandler(UserRepository userRepository, DeviceRepository deviceRepository) {
        return new DeleteDeviceCommandHandler(deviceRepository);
    }
}
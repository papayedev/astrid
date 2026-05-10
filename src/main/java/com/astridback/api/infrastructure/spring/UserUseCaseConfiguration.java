package com.astridback.api.infrastructure.spring;

import com.astridback.api.application.ports.UserRepository;
import com.astridback.api.application.usecases.users.GetMyInformationCommandHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserUseCaseConfiguration {
    @Bean
    public GetMyInformationCommandHandler getMyInformationCommandHandler(UserRepository userRepository) {
        return new GetMyInformationCommandHandler(userRepository);
    }
}

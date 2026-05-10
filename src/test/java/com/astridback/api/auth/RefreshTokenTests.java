package com.astridback.api.auth;

import com.astridback.api.UnitTests;
import com.astridback.api.application.usecases.auth.RefreshTokenCommand;
import com.astridback.api.application.usecases.auth.RefreshTokenCommandHandler;
import com.astridback.api.domain.exceptions.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RefreshTokenTests extends UnitTests {
    @BeforeEach
    public void setUp() {
        userRepository.clear();
        createFakeUser("1");
        activateUser();
    }

    private RefreshTokenCommandHandler createHandler() {
        return new RefreshTokenCommandHandler(jwtService, userRepository, apiLogger);
    }

    @Nested
    class HappyPath {
        @Test
        void shouldRefreshToken() {
            var command = new RefreshTokenCommand(createAccessAndRefreshToken().refreshToken());
            var handler = createHandler();
            var response = handler.handle(command);
            assertNotNull(response);
            assertNotNull(response.accessToken());
        }
    }

    @Nested
    class BadPath {
        @Test
        void shouldNotRefreshIfTokenIsBad() {
            var command = new RefreshTokenCommand("");
            var handler = createHandler();
            var exception = assertThrows(UnauthorizedException.class, () -> handler.handle(command));
            assertEquals("You are not authorized to perform this action", exception.getMessage());
        }
    }
}

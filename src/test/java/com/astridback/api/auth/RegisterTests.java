package com.astridback.api.auth;

import com.astridback.api.UnitTests;
import com.astridback.api.application.usecases.auth.RegisterCommand;
import com.astridback.api.application.usecases.auth.RegisterCommandHandler;
import com.astridback.api.domain.exceptions.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RegisterTests extends UnitTests {
    @BeforeEach
    public void setUp() {
        userRepository.clear();
        createFakeUser("1");
    }

    private RegisterCommandHandler createHandler() {
        return new RegisterCommandHandler(userRepository, passwordHasher, mailer, apiLogger);
    }

    @Nested
    class HappyPath {
        @Test
        void shouldRegisterTheUser() {
            var command = new RegisterCommand("correct@example.fr", "CorrectPassword123456789@");
            var handler = createHandler();
            var response = handler.handle(command);
            assertNotNull(response.id());
            var user = userRepository.findById(response.id());
            assertTrue(user.isPresent());
            assertEquals(command.emailAddress(), user.get().getEmailAddress());
            assertNotEquals(command.password(), user.get().getPassword());
            assertNotNull(user.get().getVerificationCode());
            assertFalse(user.get().isActive());

            verify(mailer, times(1)).sendVerificationCode(command.emailAddress(), user.get().getVerificationCode());
        }
    }

    @Nested
    class BadPath {
        @Test
        void shouldNotRegisterTheUserIfTheEmailIsAlreadyUsed() {
            var command = new RegisterCommand("already@example.fr", "AlreadyExist123456789@");
            var handler = createHandler();
            var exception = assertThrows(UnauthorizedException.class, () -> handler.handle(command));
            assertEquals("You are not authorized to perform this action", exception.getMessage());
        }
    }
}

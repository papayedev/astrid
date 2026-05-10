package com.astridback.api.auth;

import com.astridback.api.UnitTests;
import com.astridback.api.application.usecases.auth.ActivateAccountCommand;
import com.astridback.api.application.usecases.auth.ActivateAccountCommandHandler;
import com.astridback.api.domain.exceptions.BadRequestException;
import com.astridback.api.domain.exceptions.ForbiddenException;
import com.astridback.api.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ActivateAccountTests extends UnitTests {
    private String code;

    @BeforeEach
    public void setUp() {
        userRepository.clear();
        var user = createFakeUser("1");
        code = user.getVerificationCode();
    }

    private ActivateAccountCommandHandler createHandler() {
        return new ActivateAccountCommandHandler(userRepository, apiLogger);
    }

    @Nested
    class HappyPath {
        @Test
        public void shouldActivateTheAccount() {
            var command = new ActivateAccountCommand("already@example.fr", code);
            var handler = createHandler();
            var result = handler.handle(command);
            assertNotNull(result);
            assertEquals("1", result.id());
            var user = userRepository.findById(result.id()).get();
            assertTrue(user.isActive());
        }
    }

    @Nested
    class BadPath {
        @Test
        public void shouldNotActivateTheAccountIfTheUserDoesNotExist() {
            var command = new ActivateAccountCommand("error@example.fr", code);
            var handler = createHandler();
            var exception = assertThrows(NotFoundException.class, () -> handler.handle(command));
            assertEquals("User was not found", exception.getMessage());
        }

        @Test
        public void shouldNotActivateTheAccountIfTheUserIsAlreadyActivated() {
            var command = new ActivateAccountCommand("already@example.fr", code);
            activateUser();
            var handler = createHandler();
            var exception = assertThrows(ForbiddenException.class, () -> handler.handle(command));
            assertEquals("User is already active", exception.getMessage());
        }

        @Test
        public void shouldNotActivateTheAccountIfTheCodeIsBad() {
            var command = new ActivateAccountCommand("already@example.fr", "123");
            var handler = createHandler();
            var exception = assertThrows(BadRequestException.class, () -> handler.handle(command));
            assertEquals("Verification code is wrong", exception.getMessage());
        }

    }
}

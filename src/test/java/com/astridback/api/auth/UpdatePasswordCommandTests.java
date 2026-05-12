package com.astridback.api.auth;

import com.astridback.api.UnitTests;
import com.astridback.api.application.usecases.auth.UpdatePasswordCommand;
import com.astridback.api.application.usecases.auth.UpdatePasswordCommandHandler;
import com.astridback.api.domain.exceptions.NotFoundException;
import com.astridback.api.domain.exceptions.UnauthorizedException;
import com.astridback.api.domain.valueobject.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UpdatePasswordCommandTests extends UnitTests {
    private String verificationCode;

    @BeforeEach
    public void setUp() {
        userRepository.clear();
        var user = createFakeUser("1", Role.VISITOR);
        verificationCode = user.getVerificationCode();
    }

    private UpdatePasswordCommandHandler createHandler() {
        return new UpdatePasswordCommandHandler(userRepository, passwordHasher, apiLogger);
    }

    @Nested
    class HappyPath {
        @Test
        public void shouldUpdatePassword() {
            activateUser();
            var beforeUser = userRepository.findById("1").get();

            var command = new UpdatePasswordCommand("already@example.fr", verificationCode, "NewPassword123456789@");
            var handler = createHandler();
            handler.handle(command);

            var afterUser = userRepository.findById("1").get();

            assertNotEquals(beforeUser.getPassword(), afterUser.getPassword());
            assertNull(afterUser.getVerificationCode());
            assertNull(afterUser.getVerificationCodeExpirationDate());
        }
    }

    @Nested
    class BadPath {
        @Test
        public void shouldNotUpdatePasswordIfUserDoesNotExist() {
            var command = new UpdatePasswordCommand("error@example.fr", "12334", "NewPassword123456789@");
            var handler = createHandler();
            var exception = assertThrows(NotFoundException.class, () -> handler.handle(command));
            assertEquals("User was not found", exception.getMessage());
        }

        @Test
        public void shouldNotUpdatePasswordIfVerificationCodeIsBad() {
            activateUser();
            var command = new UpdatePasswordCommand("already@example.fr", "12334", "NewPassword123456789@");
            var handler = createHandler();
            var exception = assertThrows(UnauthorizedException.class, () -> handler.handle(command));
            assertEquals("You are not authorized to perform this action", exception.getMessage());
        }

        @Test
        public void shouldNotUpdatePasswordIfVerificationCodeIsExpired() {
            activateUser();
            expiredVerificationCode();
            var command = new UpdatePasswordCommand("already@example.fr", verificationCode, "NewPassword123456789@");
            var handler = createHandler();
            var exception = assertThrows(UnauthorizedException.class, () -> handler.handle(command));
            assertEquals("You are not authorized to perform this action", exception.getMessage());
        }

        @Test
        public void shouldNotUpdatePasswordIfPasswordIsInvalid() {
            activateUser();
            var command = new UpdatePasswordCommand("already@example.fr", verificationCode, "1@");
            var handler = createHandler();
            assertThrows(IllegalArgumentException.class, () -> handler.handle(command));
        }
    }
}

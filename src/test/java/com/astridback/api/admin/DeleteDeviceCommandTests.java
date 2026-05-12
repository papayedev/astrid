package com.astridback.api.admin;

import com.astridback.api.UnitTests;
import com.astridback.api.application.usecases.admin.DeleteDeviceCommand;
import com.astridback.api.application.usecases.admin.DeleteDeviceCommandHandler;
import com.astridback.api.domain.exceptions.NotFoundException;
import com.astridback.api.domain.exceptions.UnauthorizedException;
import com.astridback.api.domain.valueobject.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteDeviceCommandTests extends UnitTests {
    @BeforeEach
    public void setUp() {
        userRepository.clear();
        deviceRepository.clear();
    }

    private DeleteDeviceCommandHandler createHandler() {
        return new DeleteDeviceCommandHandler(deviceRepository);
    }

    @Nested
    class HappyPath {
        @Test
        public void shouldDeleteDevice() {
            createFakeUser("1", Role.ADMIN);
            createFakeDevice("1", "1");
            var command = new DeleteDeviceCommand(
                    "1",
                    Role.ADMIN
            );

            var handler = createHandler();
            handler.handle(command);

            var maybeDevice = deviceRepository.findById("1");
            assertTrue(maybeDevice.isEmpty());
        }
    }

    @Nested
    class BadPath {
        @Test
        public void shouldThrowWhenUserIsNotAdmin() {
            createFakeUser("1", Role.VISITOR);
            createFakeDevice("1", "1");

            var command = new DeleteDeviceCommand(
                    "1",
                    Role.VISITOR
            );

            var handler = createHandler();

            var exception = assertThrows(UnauthorizedException.class, () -> handler.handle(command));
            assertNotNull(exception);
            assertEquals("You are not authorized to perform this action", exception.getMessage());
        }

        @Test
        public void shouldThrowWhenDeviceIsNotFound() {
            createFakeUser("1", Role.ADMIN);

            var command = new DeleteDeviceCommand(
                    "1",
                    Role.ADMIN
            );

            var handler = createHandler();

            var exception = assertThrows(NotFoundException.class, () -> handler.handle(command));
            assertNotNull(exception);
            assertEquals("Not found", exception.getMessage());
        }
    }
}

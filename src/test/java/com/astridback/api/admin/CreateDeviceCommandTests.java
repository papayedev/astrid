package com.astridback.api.admin;

import com.astridback.api.UnitTests;
import com.astridback.api.application.usecases.admin.CreateDeviceCommand;
import com.astridback.api.application.usecases.admin.CreateDeviceCommandHandler;
import com.astridback.api.domain.exceptions.ForbiddenException;
import com.astridback.api.domain.exceptions.UnauthorizedException;
import com.astridback.api.domain.valueobject.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreateDeviceCommandTests extends UnitTests {
    @BeforeEach
    public void setup() {
        userRepository.clear();
        deviceRepository.clear();
        createFakeUser("1", Role.ADMIN);
    }

    private CreateDeviceCommandHandler createHandler() {
        return new CreateDeviceCommandHandler(deviceRepository);
    }

    @Nested
    class HappyPath {
        @Test
        public void shouldCreateDevice() {
            var command = new CreateDeviceCommand(
                    "Lulu",
                    Role.ADMIN,
                    "1234567890",
                    "1234567890"
            );

            var handler = createHandler();
            var result = handler.handle(command);

            assertNotNull(result);

            var device = deviceRepository.findByName("Lulu").get();

            assertEquals("Lulu", device.getName());
            assertEquals("1234567890", device.getSerialNumber().getValue());
            assertEquals("1234567890", device.getSerialPin().getValue());
         }
    }

    @Nested
    class BadPath {
        @Test
        public void shouldThrowWhenUserIsNotAdmin() {
            var command = new CreateDeviceCommand(
                    "Lulu",
                    Role.VISITOR,
                    "1234567890",
                    "1234567890"
            );

            var handler = createHandler();

            var exception = assertThrows(UnauthorizedException.class, () -> handler.handle(command));

            assertNotNull(exception);
        }

        @Test
        public void shouldThrowWhenNameIsAlreadyUse() {
            createFakeUser("2", Role.ADMIN);
            createFakeDevice("1", "1");

            var command = new CreateDeviceCommand(
                    "Lulu",
                    Role.ADMIN,
                    "1234567891",
                    "1234567890"
            );

            var handler = createHandler();

            var exception = assertThrows(ForbiddenException.class, () -> handler.handle(command));

            assertNotNull(exception);
            assertEquals("Name is already use", exception.getMessage());
        }

        @Test
        public void shouldThrowWhenSerialNumberIsAlreadyUse() {
            createFakeUser("2", Role.ADMIN);
            createFakeDevice("1", "1");

            var command = new CreateDeviceCommand(
                    "Lulu1",
                    Role.ADMIN,
                    "1234567890",
                    "1234567890"
            );

            var handler = createHandler();

            var exception = assertThrows(ForbiddenException.class, () -> handler.handle(command));

            assertNotNull(exception);
            assertEquals("Serial number is already use", exception.getMessage());
        }
    }
}

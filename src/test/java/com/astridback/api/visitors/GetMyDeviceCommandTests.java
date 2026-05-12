package com.astridback.api.visitors;

import com.astridback.api.UnitTests;
import com.astridback.api.application.usecases.visitors.GetMyDeviceCommand;
import com.astridback.api.application.usecases.visitors.GetMyDeviceCommandHandler;
import com.astridback.api.domain.exceptions.NotFoundException;
import com.astridback.api.domain.valueobject.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GetMyDeviceCommandTests extends UnitTests {
    @BeforeEach
    public void setup() {
        userRepository.clear();
        deviceRepository.clear();
    }

    private GetMyDeviceCommandHandler createHandler() {
        return new GetMyDeviceCommandHandler(userRepository);
    }

    @Nested
    class HappyPath {
        @Test
        public void shouldFindDevice() {
            createFakeUser("1", Role.VISITOR);
            createFakeDevice("1", "1");

            var command = new GetMyDeviceCommand("1");
            var handler = createHandler();

            var result = handler.handle(command);

            assertNotNull(result);

            assertEquals("1", result.id());
            assertEquals("Lulu", result.name());
            assertEquals("1234567890", result.serialNumber());
            assertEquals("1234567890", result.pin());
            assertEquals("1", result.userId());
        }
    }

    @Nested
    class BadPath {
        @Test
        public void shouldNotFindDeviceWhenUserDoesNotHaveDevice() {
            createFakeUser("1", Role.VISITOR);

            var command = new GetMyDeviceCommand("1");
            var handler = createHandler();

            var exception = assertThrows(NotFoundException.class, () -> handler.handle(command));

            assertNotNull(exception);
            assertEquals("Device was not found", exception.getMessage());
        }
    }
}

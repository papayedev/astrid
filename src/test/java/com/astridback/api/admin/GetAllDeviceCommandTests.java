package com.astridback.api.admin;

import com.astridback.api.UnitTests;
import com.astridback.api.application.usecases.admin.GetAllDevicesCommand;
import com.astridback.api.application.usecases.admin.GetAllDevicesCommandHandler;
import com.astridback.api.domain.exceptions.UnauthorizedException;
import com.astridback.api.domain.valueobject.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GetAllDeviceCommandTests extends UnitTests {
    @BeforeEach
    public void setUp() {
        userRepository.clear();
        deviceRepository.clear();
        createFakeUser("1", Role.ADMIN);
        createFakeDevice("1", "1");
    }

    private GetAllDevicesCommandHandler createHandler() {
        return new GetAllDevicesCommandHandler(deviceRepository);
    }

    @Nested
    class HappyPath {
        @Test
        public void shouldGetAllDevices() {
            var command = new GetAllDevicesCommand(
                    Role.ADMIN,
                    0
            );
            var handler = createHandler();
            var result = handler.handle(command);

            assertNotNull(result);
            assertEquals("1", result.maxPage());
            assertEquals(1, result.devices().size());
        }
    }

    @Nested
    class BadPath {
        @Test
        public void shouldThrowWhenUserIsNotAdmin() {
            createFakeUser("2", Role.VISITOR);
            createFakeDevice("2", "2");

            var command = new GetAllDevicesCommand(
                    Role.VISITOR,
                    0
            );

            var handler = createHandler();

            var exception = assertThrows(UnauthorizedException.class, () -> handler.handle(command));

            assertEquals("You are not authorized to perform this action", exception.getMessage());
        }
    }
}

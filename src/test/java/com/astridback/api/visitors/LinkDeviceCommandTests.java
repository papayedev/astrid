package com.astridback.api.visitors;

import com.astridback.api.UnitTests;
import com.astridback.api.application.usecases.visitors.LinkDeviceCommand;
import com.astridback.api.application.usecases.visitors.LinkDeviceCommandHandler;
import com.astridback.api.domain.exceptions.NotFoundException;
import com.astridback.api.domain.exceptions.UnauthorizedException;
import com.astridback.api.domain.valueobject.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LinkDeviceCommandTests extends UnitTests {
    @BeforeEach
    public void setup() {
        userRepository.clear();
        deviceRepository.clear();
    }

    protected LinkDeviceCommandHandler createHandler() {
        return new LinkDeviceCommandHandler(userRepository, deviceRepository);
    }

    @Nested
    class HappyPath {
        @Test
        public void shouldLinkDevice() {
            createFakeUser("1", Role.ADMIN);
            createFakeDevice("1");

            var command = new LinkDeviceCommand(
                    "1",
                    "Lulu2",
                    "1234567890",
                    "1234567890"
            );

            var handler = createHandler();
            handler.handle(command);

            var user = userRepository.findById("1").get();
            assertNotNull(user.getDevice());
            assertEquals("1", user.getDevice().getId());
            assertEquals("Lulu2", user.getDevice().getName());;
            assertEquals("1234567890", user.getDevice().getSerialNumber().getValue());
            assertEquals("1234567890", user.getDevice().getSerialPin().getValue());
        }
    }

    @Nested
    class BadPath {
        @Test
        public void shouldThrowWhenUserHasAnDevice() {
            createFakeUser("1", Role.ADMIN);
            createFakeDevice("1", "1");

            var command = new LinkDeviceCommand(
                    "1",
                    "Lulu",
                    "1234567890",
                    "1234567890"
            );

            var handler = createHandler();

            var exception = assertThrows(UnauthorizedException.class, () ->  handler.handle(command));
            assertNotNull(exception);
            assertEquals("You have already device", exception.getMessage());
        }

        @Test
        public void shouldThrowWhenSerialIsNotFound() {
            createFakeUser("1", Role.ADMIN);
            createFakeDevice("1");

            var command = new LinkDeviceCommand(
                    "1",
                    "Lulu",
                    "1234567891",
                    "1234567890"
            );

            var handler = createHandler();

            var exception = assertThrows(NotFoundException.class, () ->  handler.handle(command));
            assertNotNull(exception);
            assertEquals("Serial Number was not found", exception.getMessage());
        }

        @Test
        public void shouldThrowWhenDeviceIsAlreadyAttachWithUser() {
            createFakeUser("1", Role.ADMIN);
            createFakeUser("2", Role.ADMIN);
            createFakeDevice("1", "2");

            var command = new LinkDeviceCommand(
                    "1",
                    "Lulu",
                    "1234567890",
                    "1234567890"
            );

            var handler = createHandler();

            var exception = assertThrows(UnauthorizedException.class, () ->  handler.handle(command));
            assertNotNull(exception);
            assertEquals("You are not authorized to perform this action", exception.getMessage());
        }

        @Test
        public void shouldThrowWhenPinIsWrong() {
            createFakeUser("1", Role.ADMIN);
            createFakeDevice("1");

            var command = new LinkDeviceCommand(
                    "1",
                    "Lulu",
                    "1234567890",
                    "1234567891"
            );

            var handler = createHandler();

            var exception = assertThrows(UnauthorizedException.class, () ->  handler.handle(command));
            assertNotNull(exception);
            assertEquals("Pin wrong", exception.getMessage());
        }
    }
}

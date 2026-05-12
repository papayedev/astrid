package com.astridback.api.positions;

import com.astridback.api.UnitTests;
import com.astridback.api.application.services.Base64Service;
import com.astridback.api.application.usecases.positions.ReceivePositionCommand;
import com.astridback.api.application.usecases.positions.ReceivePositionCommandHandler;
import com.astridback.api.domain.exceptions.NotFoundException;
import com.astridback.api.domain.exceptions.UnauthorizedException;
import com.astridback.api.domain.valueobject.PositionPayload;
import com.astridback.api.domain.valueobject.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class ReceivePositionCommandTests extends UnitTests {
    @Autowired
    private Base64Service<PositionPayload> base64Service;

    @BeforeEach
    public void setup() {
        userRepository.clear();
        deviceRepository.clear();
        positionRepository.clear();
    }

    private ReceivePositionCommandHandler createHandler() {
        return new ReceivePositionCommandHandler(deviceRepository, base64Service);
    }

    @Nested
    class HappyPath {
        @Test
        public void shouldReceivePosition() {
            createFakeUser("1", Role.VISITOR);
            createFakeDevice("1", "1");

            var command = new ReceivePositionCommand(
                    "eyJsb25naXR1ZGUiOiAiNTIiLCAibGF0aXR1ZGUiOiAiNTIiLCAic2VyaWFsTnVtYmVyIjogIjEyMzQ1Njc4OTAifQ=="
            );

            var secondCommand = new ReceivePositionCommand(
                    "eyJsb25naXR1ZGUiOiAiNTMiLCAibGF0aXR1ZGUiOiAiNTMiLCAic2VyaWFsTnVtYmVyIjogIjEyMzQ1Njc4OTAifQ=="
            );

            var handler = createHandler();

            handler.handle(command);
            handler.handle(secondCommand);

            var maybeDevice = deviceRepository.findById("1");

            assertTrue(maybeDevice.isPresent());

            var device = maybeDevice.get();

            assertNotNull(device.getPositions());

            assertEquals(2, device.getPositions().size());
            assertEquals("53",  device.getPositions().get(0).getLatitude());
            assertEquals("53",  device.getPositions().get(0).getLongitude());

            assertEquals("52", device.getPositions().get(1).getLatitude());
            assertEquals("52", device.getPositions().get(1).getLongitude());
        }
    }

    @Nested
    class BadPath {
        @Test
        public void shouldThrowWhenIsNotDeserializable() {
            createFakeUser("1", Role.VISITOR);
            createFakeDevice("1", "1");

            var command = new ReceivePositionCommand(
                    "eyJsb25naXR1ZGUiOiAiNTMiLCAic3MiOiAiNTMiLCAic2VyaWFsTnVtYmVyIjogIjEyMzQ1Njc4OTAifQ=="
            );

            var handler = createHandler();

            var exception = assertThrows(UnauthorizedException.class, () -> handler.handle(command));

            assertNotNull(exception);
            assertEquals("You are not authorized to perform this action", exception.getMessage());
        }

        @Test
        public void shouldThrowWhenDeviceIsNotFound() {
            createFakeUser("1", Role.ADMIN);

            var command = new ReceivePositionCommand(
                    "eyJsb25naXR1ZGUiOiAiNTIiLCAibGF0aXR1ZGUiOiAiNTIiLCAic2VyaWFsTnVtYmVyIjogIjEyMzQ1Njc4OTAifQ=="
            );

            var handler = createHandler();

            var exception = assertThrows(NotFoundException.class, () -> handler.handle(command));

            assertNotNull(exception);
            assertEquals("Not found", exception.getMessage());
        }
    }
}

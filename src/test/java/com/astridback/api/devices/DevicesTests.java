package com.astridback.api.devices;

import com.astridback.api.UnitTests;
import com.astridback.api.domain.model.Device;
import com.astridback.api.domain.valueobject.Pin;
import com.astridback.api.domain.valueobject.SerialNumber;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DevicesTests extends UnitTests {
    @Nested
    class HappyPath {
        @Test
        public void shouldReturnDevice() {
            var device = new Device(
                    "1",
                    new SerialNumber("1234567890"),
                    new Pin("1234567890"),
                    createFakeUser("1"),
                    "Lulu"
            );

            assertEquals("1", device.getId());
            assertEquals("1234567890", device.getSerialNumber().getValue());
            assertEquals("1234567890", device.getSerialPin().getValue());
            assertEquals("Lulu", device.getName());
            assertNotNull(device.getUser());
        }
    }

    @Nested
    class BadPath {
        @Test
        public void shouldThrowWhenNameIsNull() {
            var exception = assertThrows(IllegalArgumentException.class, () -> new Device(
                    "1",
                    new SerialNumber("1234567890"),
                    new Pin("1234567890"),
                    createFakeUser("1"),
                    null
            ));
            assertEquals("Name is null", exception.getMessage());
        }

        @Test
        public void shouldThrowWhenNameLengthIsMoreThanTen() {
            var exception = assertThrows(IllegalArgumentException.class, () -> new Device(
                    "1",
                    new SerialNumber("1234567890"),
                    new Pin("1234567890"),
                    createFakeUser("1"),
                    "LuluLuluLuluLuluLuluLuluLulu"
            ));
            assertEquals("Name is too long", exception.getMessage());
        }
    }
}

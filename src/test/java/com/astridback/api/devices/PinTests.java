package com.astridback.api.devices;

import com.astridback.api.UnitTests;
import com.astridback.api.domain.valueobject.Pin;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PinTests extends UnitTests {
    @Nested
    class HappyPath {
        @Test
        public void shouldReturnPin() {
            var pin = new Pin("1234567890");
            assertEquals("1234567890", pin.getValue());
        }
    }

    @Nested
    class BadPath {
        @Test
        public void shouldThrowOnNull() {
            var exception = assertThrows(IllegalArgumentException.class, () -> new Pin(null));
            assertEquals("Pin cannot be null", exception.getMessage());
        }

        @Test
        public void shouldThrowLengthIsMoreThanTen() {
            var exception = assertThrows(IllegalArgumentException.class, () -> new Pin("12345678901"));
            assertEquals("Pin cannot contain more than 10 characters", exception.getMessage());
        }
    }
}

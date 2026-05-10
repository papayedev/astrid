package com.astridback.api.devices;

import com.astridback.api.UnitTests;
import com.astridback.api.domain.valueobject.SerialNumber;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SerialNumberTests extends UnitTests {
    @Nested
    class HappyPath {
        @Test
        public void shouldReturnSerialNumber() {
            var serialNumber = new SerialNumber(
                    "1234567890"
            );

            assertEquals("1234567890", serialNumber.getValue());
        }
    }

    @Nested
    class BadPath {
        @Test
        public void shouldThrowOnNull() {
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                new SerialNumber(null);
            });

            assertEquals("Serial Number cannot be null", exception.getMessage());
        }

        @Test
        public void shouldThrowLengthIsMoreThanTen() {
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                new SerialNumber("12345678901");
            });

            assertEquals("Serial Number cannot contain more than 10 characters", exception.getMessage());
        }

        @Test
        public void shouldThrowIfNotOnlyNumerics() {
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                new SerialNumber("123456789l");
            });

            assertEquals("Serial Number can only contain numbers", exception.getMessage());
        }
    }
}

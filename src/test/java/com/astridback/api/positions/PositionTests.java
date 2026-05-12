package com.astridback.api.positions;

import com.astridback.api.UnitTests;
import com.astridback.api.domain.model.Device;
import com.astridback.api.domain.model.Position;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTests extends UnitTests {
    @Nested
    class HappyPath {
        @Test
        public void shouldCreatePosition() {
            var position = new Position(
                    "1",
                    "52",
                    "52",
                    new Device()
            );

            assertEquals("1", position.getId());
            assertEquals("52", position.getLongitude());
            assertEquals("52", position.getLatitude());
            assertNotNull( position.getDevice());
        }
    }

    @Nested
    class BadPath {
        @Test
        public void shouldThrowWhenIdIsEmpty() {
            var exception = assertThrows(IllegalArgumentException.class, () -> {
               new Position(
                       "",
                       "52",
                       "52",
                       new Device()
               );
            });

            assertNotNull(exception);
            assertEquals("Fields empty", exception.getMessage());
        }

        @Test
        public void shouldThrowWhenLongitudeIsEmpty() {
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                new Position(
                        "1",
                        "",
                        "52",
                        new Device()
                );
            });

            assertNotNull(exception);
            assertEquals("Fields empty", exception.getMessage());
        }

        @Test
        public void shouldThrowWhenLatitudeIsEmpty() {
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                new Position(
                        "1",
                        "52",
                        "",
                        new Device()
                );
            });

            assertNotNull(exception);
            assertEquals("Fields empty", exception.getMessage());
        }

        @Test
        public void shouldThrowWhenDeviceIsNull() {
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                new Position(
                        "1",
                        "52",
                        "52",
                        null
                );
            });

            assertNotNull(exception);
            assertEquals("Fields empty", exception.getMessage());
        }
    }
}

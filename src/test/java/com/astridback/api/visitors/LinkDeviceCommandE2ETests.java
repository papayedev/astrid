package com.astridback.api.visitors;

import com.astridback.api.IntegrationTests;
import com.astridback.api.domain.valueobject.Role;
import com.astridback.api.presentation.dto.LinkDeviceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class LinkDeviceCommandE2ETests extends IntegrationTests {
    @BeforeEach
    public void setup() {
        userRepository.clear();
        deviceRepository.clear();
    }

    @Nested
    class HappyPath {
        @Test
        public void shouldLinkDevice() throws Exception{
            createFakeUser("1", Role.ADMIN);
            createFakeDevice("1");

            var dto = new LinkDeviceDTO(
                    "Lulu",
                    "1234567890",
                    "1234567890"
            );

            mockMvc.perform(MockMvcRequestBuilders.post("/visitors/devices/link")
                    .header("Authorization", createJwt())
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    class BadPath {
        @Test
        public void shouldThrowWhenUserHasAnDevice() throws Exception {
            createFakeUser("1", Role.ADMIN);
            createFakeDevice("1", "1");

            var dto = new LinkDeviceDTO(
                    "Lulu",
                    "1234567890",
                    "1234567890"
            );

            mockMvc.perform(MockMvcRequestBuilders.post("/visitors/devices/link")
                            .header("Authorization", createJwt())
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }

        @Test
        public void shouldThrowWhenSerialIsNotFound() throws Exception {
            createFakeUser("1", Role.ADMIN);
            createFakeDevice("1");

            var dto = new LinkDeviceDTO(
                    "Lulu",
                    "1234567891",
                    "1234567890"
            );

            mockMvc.perform(MockMvcRequestBuilders.post("/visitors/devices/link")
                            .header("Authorization", createJwt())
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        public void shouldThrowWhenDeviceIsAlreadyAttachWithUser() throws Exception {
            createFakeUser("1", Role.ADMIN);
            createFakeUser("2", Role.ADMIN);
            createFakeDevice("1", "2");

            var dto = new LinkDeviceDTO(
                    "Lulu",
                    "1234567890",
                    "1234567890"
            );

            mockMvc.perform(MockMvcRequestBuilders.post("/visitors/devices/link")
                            .header("Authorization", createJwt())
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        }

        @Test
        public void shouldThrowWhenPinIsWrong() throws Exception{
            createFakeUser("1", Role.ADMIN);
            createFakeDevice("1");

            var dto = new LinkDeviceDTO(
                    "Lulu",
                    "1234567890",
                    "1234567891"
            );

            mockMvc.perform(MockMvcRequestBuilders.post("/visitors/devices/link")
                            .header("Authorization", createJwt())
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }
    }
}

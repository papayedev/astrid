package com.astridback.api.admin;

import com.astridback.api.IntegrationTests;
import com.astridback.api.domain.valueobject.Role;
import com.astridback.api.domain.viewmodel.IdResponse;
import com.astridback.api.presentation.dto.CreateDeviceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateDeviceCommandE2ETests extends IntegrationTests {
    @BeforeEach
    public void beforeEach() {
        userRepository.clear();
        deviceRepository.clear();
    }

    @Nested
    class HappyPath {
        @Test
        public void shouldCreateDevice() throws Exception{
            createFakeUser("1", Role.ADMIN);

            var dto = new CreateDeviceDTO(
                    "Lulu",
                    "1234567890",
                    "1234567890"
            );

            var response = mockMvc.perform(MockMvcRequestBuilders.post("/admin/devices")
                            .header("Authorization", createJwt())
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andReturn();

            var result = objectMapper.readValue(response.getResponse().getContentAsString(), IdResponse.class);

            assertNotNull(result);
            assertTrue(deviceRepository.findByName("Lulu").isPresent());
        }
    }

    @Nested
    class BadPath {
        @Test
        public void shouldThrowWhenUserIsNotAdmin() throws Exception {
            createFakeUser("1", Role.VISITOR);

            var dto = new CreateDeviceDTO(
                    "Lulu",
                    "1234567890",
                    "1234567890"
            );

            mockMvc.perform(MockMvcRequestBuilders.post("/admin/devices")
                            .header("Authorization", createJwt())
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }

        @Test
        public void shouldThrowWhenNameIsAlreadyUse() throws Exception {
            createFakeUser("1", Role.ADMIN);
            createFakeUser("2", Role.ADMIN);
            createFakeDevice("2", "2");

            var dto = new CreateDeviceDTO(
                    "Lulu",
                    "1234567890",
                    "1234567890"
            );

            mockMvc.perform(MockMvcRequestBuilders.post("/admin/devices")
                            .header("Authorization", createJwt())
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());

        }

        @Test
        public void shouldThrowWhenSerialNumberIsAlreadyUse() throws Exception {
            createFakeUser("1", Role.ADMIN);
            createFakeUser("2", Role.ADMIN);
            createFakeDevice("2", "2");

            var dto = new CreateDeviceDTO(
                    "Lulu1",
                    "1234567890",
                    "1234567890"
            );

            mockMvc.perform(MockMvcRequestBuilders.post("/admin/devices")
                            .header("Authorization", createJwt())
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
        }
    }
}

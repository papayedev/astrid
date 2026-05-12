package com.astridback.api.admin;

import com.astridback.api.IntegrationTests;
import com.astridback.api.domain.valueobject.Role;
import com.astridback.api.domain.viewmodel.AllDeviceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GetAllDeviceCommandE2ETests extends IntegrationTests {
    @BeforeEach
    void setup() {
        userRepository.clear();
        deviceRepository.clear();
        createFakeUser("1", Role.ADMIN);
        createFakeDevice("1", "1");
    }

    @Nested
    class HappyPath {
        @Test
        public void shouldGetAllDevices() throws Exception {
            var response = mockMvc.perform(MockMvcRequestBuilders.get("/admin/devices")
                    .contentType("application/json")
                    .header("Authorization", createJwt("1")))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            assertNotNull(response);

            var result = objectMapper.readValue(response.getResponse().getContentAsString(), AllDeviceResponse.class);

            assertNotNull(result);
        }
    }

    @Nested
    class BadPath {
        @Test
        public void shouldThrowWhenUserIsNotAdmin() throws Exception {
            createFakeUser("2", Role.VISITOR);
            createFakeDevice("2", "2");

            mockMvc.perform(MockMvcRequestBuilders.get("/admin/devices")
                            .header("Authorization", createJwt("2")))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }
    }
}

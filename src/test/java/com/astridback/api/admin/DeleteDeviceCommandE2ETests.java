package com.astridback.api.admin;

import com.astridback.api.IntegrationTests;
import com.astridback.api.domain.valueobject.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class DeleteDeviceCommandE2ETests extends IntegrationTests {
    @BeforeEach
    public void setUp() {
        userRepository.clear();
        deviceRepository.clear();
    }

    @Nested
    class HappyPath {
        @Test
        public void shouldDeleteDevice() throws Exception {
            createFakeUser("1", Role.ADMIN);
            createFakeDevice("1", "1");

            mockMvc.perform(MockMvcRequestBuilders.delete("/admin/devices/1")
                            .header("Authorization", createJwt())
                    )
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    class BadPath {
        @Test
        public void shouldThrowWhenUserIsNotAdmin() throws Exception {
            createFakeUser("1", Role.VISITOR);
            createFakeDevice("1", "1");

            mockMvc.perform(MockMvcRequestBuilders.delete("/admin/devices/1")
                            .header("Authorization", createJwt())
                    )
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }

        @Test
        public void shouldThrowWhenDeviceIsNotFound() throws Exception {
            createFakeUser("1", Role.ADMIN);
            mockMvc.perform(MockMvcRequestBuilders.delete("/admin/devices/1")
                            .header("Authorization", createJwt())
                    )
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }
    }
}

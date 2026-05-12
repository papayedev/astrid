package com.astridback.api.visitors;

import com.astridback.api.IntegrationTests;
import com.astridback.api.domain.valueobject.Role;
import com.astridback.api.domain.viewmodel.DeviceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GetMyDeviceCommandE2ETests extends IntegrationTests {
    @BeforeEach
    public void setup() {
        userRepository.clear();
        deviceRepository.clear();
    }

    @Nested
    class HappyPath {
        @Test
        public void shouldFineDevice() throws Exception {
            createFakeUser("1", Role.VISITOR);
            createFakeDevice("1", "1");

            var response = mockMvc.perform(MockMvcRequestBuilders.get("/visitors/devices")
                    .header("Authorization", createJwt()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            var result = objectMapper.readValue(response.getResponse().getContentAsString(), DeviceResponse.class);

            assertNotNull(result);
        }
    }

    @Nested
    class BadPath {
        @Test
        public void shouldNotFineDeviceWhenUserDoesNotHaveDevice() throws Exception {
            createFakeUser("1", Role.VISITOR);

            mockMvc.perform(MockMvcRequestBuilders.get("/visitors/devices")
                            .header("Authorization", createJwt()))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }
    }
}

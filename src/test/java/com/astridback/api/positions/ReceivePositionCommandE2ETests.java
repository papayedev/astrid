package com.astridback.api.positions;

import com.astridback.api.IntegrationTests;
import com.astridback.api.application.services.Base64Service;
import com.astridback.api.domain.valueobject.PositionPayload;
import com.astridback.api.domain.valueobject.Role;
import com.astridback.api.presentation.dto.ReceivePositionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class ReceivePositionCommandE2ETests extends IntegrationTests {
    @Autowired
    private Base64Service<PositionPayload> base64Service;

    @BeforeEach
    public void setup() {
        userRepository.clear();
        deviceRepository.clear();
        positionRepository.clear();
    }

    @Nested
    class HappyPath {
        @Test
        public void shouldReceivePosition() throws Exception {
            createFakeUser("1", Role.VISITOR);
            createFakeDevice("1", "1");

            var dto = new ReceivePositionDTO("eyJsb25naXR1ZGUiOiAiNTIiLCAibGF0aXR1ZGUiOiAiNTIiLCAic2VyaWFsTnVtYmVyIjogIjEyMzQ1Njc4OTAifQ==");

            mockMvc.perform(MockMvcRequestBuilders.post("/positions")
                    .header("X-Astrocast-Key", "secret")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    class BadPath {
        @Test
        public void shouldThrowWhenUserIsNotAuthenticate() throws Exception {
            createFakeUser("1", Role.VISITOR);
            createFakeDevice("1", "1");

            var dto = new ReceivePositionDTO("eyJsb25naXR1ZGUiOiAiNTIiLCAibGF0aXR1ZGUiOiAiNTIiLCAic2VyaWFsTnVtYmVyIjogIjEyMzQ1Njc4OTAifQ==");

            mockMvc.perform(MockMvcRequestBuilders.post("/positions")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
        }

        @Test
        public void shouldThrowWhenIsNotDeserializable() throws Exception {
            createFakeUser("1", Role.VISITOR);
            createFakeDevice("1", "1");

            var dto = new ReceivePositionDTO("eyJsb25naXR1ZGUiOiAiNTMiLCAic3MiOiAiNTMiLCAic2VyaWFsTnVtYmVyIjogIjEyMzQ1Njc4OTAifQ==");

            mockMvc.perform(MockMvcRequestBuilders.post("/positions")
                            .header("X-Astrocast-Key", "secret")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        }

        @Test
        public void shouldThrowWhenDeviceIsNotFound() throws Exception {
            createFakeUser("1", Role.VISITOR);

            var dto = new ReceivePositionDTO("eyJsb25naXR1ZGUiOiAiNTIiLCAibGF0aXR1ZGUiOiAiNTIiLCAic2VyaWFsTnVtYmVyIjogIjEyMzQ1Njc4OTAifQ==");

            mockMvc.perform(MockMvcRequestBuilders.post("/positions")
                            .header("X-Astrocast-Key", "secret")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }
    }
}

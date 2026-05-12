package com.astridback.api.auth;

import com.astridback.api.IntegrationTests;
import com.astridback.api.domain.valueobject.Role;
import com.astridback.api.domain.viewmodel.IdResponse;
import com.astridback.api.presentation.dto.ActiveAccountDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ActivateAccountE2ETests extends IntegrationTests {
    private String code;

    @BeforeEach
    public void setUp() {
        userRepository.clear();
        var user = createFakeUser("1", Role.VISITOR);
        code = user.getVerificationCode();
    }

    @Nested
    class HappyPath {
        @Test
        public void shouldActivateTheAccount() throws Exception {
            var dto = new ActiveAccountDTO("already@example.fr", code);

            var response = mockMvc.perform(MockMvcRequestBuilders.post("/auth/activate")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsBytes(dto)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            var idResponse = objectMapper.readValue(response.getResponse().getContentAsString(), IdResponse.class);

            assertNotNull(idResponse);
            assertEquals("1", idResponse.id());
        }
    }

    @Nested
    class BadPath {
        @Test
        public void shouldNotActivateTheAccountIfTheUserDoesNotExist() throws Exception {
            var dto = new ActiveAccountDTO("error@example.fr", code);
            mockMvc.perform(MockMvcRequestBuilders.post("/auth/activate")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsBytes(dto)))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        public void shouldNotActivateTheAccountIfTheCodeIsBad() throws Exception {
            var dto = new ActiveAccountDTO("already@example.fr", code);
            activateUser();
            mockMvc.perform(MockMvcRequestBuilders.post("/auth/activate")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsBytes(dto)))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
        }

        @Test
        public void shouldNotActivateTheAccountIfTheUserIsAlreadyActivated() throws Exception {
            var dto = new ActiveAccountDTO("already@example.fr", "123");
            mockMvc.perform(MockMvcRequestBuilders.post("/auth/activate")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsBytes(dto)))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }
    }
}

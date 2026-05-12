package com.astridback.api.auth;

import com.astridback.api.IntegrationTests;
import com.astridback.api.domain.valueobject.Role;
import com.astridback.api.presentation.dto.ResetPasswordRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class ResetPasswordRequestsE2ETests extends IntegrationTests {
    @BeforeEach
    public void setUp() {
        userRepository.clear();
        createFakeUser("1", Role.VISITOR);
    }

    @Nested
    class HappyPath {
        @Test
        public void shouldSendRequestPasswordReset() throws Exception {
            activateUser();
            var dto = new ResetPasswordRequestDTO("already@example.fr");

            mockMvc.perform(MockMvcRequestBuilders.post("/auth/request/password")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsBytes(dto)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();
        }
    }

    @Nested
    class BadPath {
        @Test
        public void shouldNotSendRequestPasswordResetIfUserDoesNotExist() throws Exception {
            var dto = new ResetPasswordRequestDTO("error@example.fr");

            mockMvc.perform(MockMvcRequestBuilders.post("/auth/request/password")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsBytes(dto)))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn();
        }

        @Test
        public void shouldNotSendRequestPasswordResetIfUserIsAlreadyVerified() throws Exception {
            var dto = new ResetPasswordRequestDTO("already@example.fr");

            mockMvc.perform(MockMvcRequestBuilders.post("/auth/request/password")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsBytes(dto)))
                    .andExpect(MockMvcResultMatchers.status().isForbidden())
                    .andReturn();
        }
    }
}
package com.astridback.api.auth;

import com.astridback.api.IntegrationTests;
import com.astridback.api.presentation.dto.UpdatePasswordDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class UpdatePasswordE2ETests extends IntegrationTests {
    private String verificationCode;

    @BeforeEach
    public void setUp() {
        userRepository.clear();
        var user = createFakeUser("1");
        verificationCode = user.getVerificationCode();
    }

    @Nested
    class HappyPath {
        @Test
        public void shouldUpdatePassword() throws Exception {
            activateUser();
            var dto = new UpdatePasswordDTO("already@example.fr", verificationCode, "NewPassword123456789@");

            mockMvc.perform(MockMvcRequestBuilders.put("/auth/update/password")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsBytes(dto)))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    class BadPath {
        @Test
        public void shouldNotUpdatePasswordIfTheUserDoesNotExist() throws Exception {
            activateUser();
            var dto = new UpdatePasswordDTO("error@example.fr", verificationCode, "NewPassword123456789@");

            mockMvc.perform(MockMvcRequestBuilders.put("/auth/update/password")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsBytes(dto)))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        public void shouldNotUpdatePasswordIfTheCodeIsBad() throws Exception {
            activateUser();
            var dto = new UpdatePasswordDTO("already@example.fr", "1234", "NewPassword123456789@");

            mockMvc.perform(MockMvcRequestBuilders.put("/auth/update/password")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsBytes(dto)))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }

        @Test
        public void shouldNotUpdatePasswordIfTheCodeIsExpired() throws Exception {
            activateUser();
            expiredVerificationCode();

            var dto = new UpdatePasswordDTO("already@example.fr", verificationCode, "NewPassword123456789@");

            mockMvc.perform(MockMvcRequestBuilders.put("/auth/update/password")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsBytes(dto)))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }

        @Test
        public void shouldNotUpdatePasswordIfThePasswordIsInvalid() throws Exception {
            var dto = new UpdatePasswordDTO("already@example.fr", verificationCode, "1@");

            mockMvc.perform(MockMvcRequestBuilders.put("/auth/update/password")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsBytes(dto)))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
        }
    }
}

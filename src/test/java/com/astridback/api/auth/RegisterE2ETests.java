package com.astridback.api.auth;

import com.astridback.api.IntegrationTests;
import com.astridback.api.application.ports.UserRepository;
import com.astridback.api.domain.model.User;
import com.astridback.api.domain.viewmodel.IdResponse;
import com.astridback.api.presentation.dto.RegisterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterE2ETests extends IntegrationTests {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.clear();
        var user = new User(
                UUID.randomUUID().toString(),
                "already@example.fr",
                "Azerty123456789@", null);
        user.resetPassword(passwordHasher.hash("Azerty123456789@"));
        user.createVerificationCode(15);
        userRepository.save(user);
    }

    @Nested
    class HappyPath {
        @Test
        void shouldRegisterTheUser() throws Exception {
            var dto = new RegisterDTO(
                    "contact@example.fr",
                    "Azerty123456789@");

            var result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(dto)))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andReturn();

            var idResponse = objectMapper.readValue(result.getResponse().getContentAsString(), IdResponse.class);

            assertNotNull(idResponse.id());

            var user = userRepository.findById(idResponse.id()).orElseThrow();

            assertEquals(dto.email(), user.getEmailAddress());
            assertNotEquals(dto.password(), user.getPassword());
            assertNotNull(user.getVerificationCode());
            assertNotNull(user.getVerificationCodeExpirationDate());
            assertFalse(user.isActive());
        }
    }

    @Nested
    class BadPath {
        @Test
        public void shouldReturn400WhenEmailIsInvalid() throws Exception {
            var dto = new RegisterDTO("not-an-email", "Azerty123456789@");

            mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(dto)))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        public void shouldReturn400WhenPasswordIsInvalid() throws Exception {
            var dto = new RegisterDTO("second@example.fr", "azerty");

            mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(dto)))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        public void shouldReturn401WhenUserAlreadyExists() throws Exception {
            var dto = new RegisterDTO("already@example.fr", "Azerty123456789@");

            mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(dto)))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }
    }
}
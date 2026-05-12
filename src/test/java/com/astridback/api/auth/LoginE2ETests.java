package com.astridback.api.auth;

import com.astridback.api.IntegrationTests;
import com.astridback.api.domain.valueobject.Role;
import com.astridback.api.domain.viewmodel.LoggedInUserViewModel;
import com.astridback.api.presentation.dto.LoginDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginE2ETests extends IntegrationTests {
    @BeforeEach
    public void setUp() {
        userRepository.clear();
        createFakeUser("1", Role.VISITOR);
    }

    @Nested
    class HappyPath {
        @Test
        public void shouldLogin() throws Exception {
            activateUser();

            var dto = new LoginDTO("already@example.fr", "Azerty123456789@");

            var response = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsBytes(dto)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            var loggedInUserView = objectMapper.readValue(response.getResponse().getContentAsString(),
                    LoggedInUserViewModel.class);

            assertNotNull(loggedInUserView);
            assertEquals("1", loggedInUserView.id());
            assertEquals("already@example.fr", loggedInUserView.emailAddress());
            assertNotNull(loggedInUserView.accessToken());
            assertNotNull(loggedInUserView.refreshToken());
        }
    }

    @Nested
    class BadPath {
        @Test
        public void shouldNotLoginIfUserDoesNotExist() throws Exception {
            var dto = new LoginDTO("contact@example.fr", "Azerty123456789@");
            mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsBytes(dto)))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn();
        }

        @Test
        public void shouldNotLoginIfPasswordIsWrong() throws Exception {
            var dto = new LoginDTO("already@example.fr", "Azerty1@");
            mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsBytes(dto)))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                    .andReturn();
        }

        @Test
        public void shouldNotLoginIfUserIsInactive() throws Exception {
            var dto = new LoginDTO("already@example.fr", "Azerty123456789@");
            mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsBytes(dto)))
                    .andExpect(MockMvcResultMatchers.status().isForbidden())
                    .andReturn();
        }
    }
}

package com.astridback.api.users;

import com.astridback.api.IntegrationTests;
import com.astridback.api.domain.valueobject.Role;
import com.astridback.api.domain.viewmodel.UserDetailsViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GetMyInformationE2ETests extends IntegrationTests {
    @BeforeEach
    public void setup() {
        userRepository.clear();
        createFakeUser("1", Role.VISITOR);
    }

    @Nested
    class HappyPath {
        @Test
        public void shouldGetUserDetails() throws Exception{
            var response = mockMvc.perform(MockMvcRequestBuilders.get("/users")
                    .contentType("application/json")
                    .header("Authorization", createJwt()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            var userDetails = objectMapper.readValue(response.getResponse().getContentAsString(), UserDetailsViewModel.class);

            assertNotNull(userDetails);
        }
    }
}

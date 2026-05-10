package com.astridback.api.users;

import com.astridback.api.UnitTests;
import com.astridback.api.application.usecases.users.GetMyInformationCommand;
import com.astridback.api.application.usecases.users.GetMyInformationCommandHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GetMyInformationTests extends UnitTests {
    @BeforeEach
    public void setup() {
        userRepository.clear();
        createFakeUser("1");
    }

    private GetMyInformationCommandHandler createHandler() {
        return new  GetMyInformationCommandHandler(userRepository);
    }

    @Nested
    class HappyPath {
        @Test
        public void shouldReturnUsersDetails() {
            var command = new GetMyInformationCommand("1");
            var handler = createHandler();
            var result = handler.handle(command);
            assertNotNull(result);
            assertEquals("already@example.fr", result.emailAddress());
            assertEquals("VISITOR", result.role());
        }
    }
}

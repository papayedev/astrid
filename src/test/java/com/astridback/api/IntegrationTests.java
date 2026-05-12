package com.astridback.api;

import com.astridback.api.application.ports.DeviceRepository;
import com.astridback.api.application.ports.PositionRepository;
import com.astridback.api.application.ports.UserRepository;
import com.astridback.api.application.services.JwtService;
import com.astridback.api.application.services.PasswordHasher;
import com.astridback.api.domain.model.Device;
import com.astridback.api.domain.model.User;
import com.astridback.api.domain.valueobject.Pin;
import com.astridback.api.domain.valueobject.Role;
import com.astridback.api.domain.valueobject.SerialNumber;
import com.astridback.api.domain.viewmodel.LoggedInUserViewModel;
import com.astridback.api.infrastructure.services.BcryptPasswordHasher;
import com.astridback.api.core.application.ports.Mailer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@Import(PostgresSQLTestConfiguration.class)
@AutoConfigureMockMvc
@Transactional
public class IntegrationTests {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected DeviceRepository deviceRepository;

    @Autowired
    protected PositionRepository positionRepository;

    @Autowired
    protected JwtService jwtService;

    @MockitoBean
    protected Mailer mailer;

    protected PasswordHasher passwordHasher = new BcryptPasswordHasher();

    protected User createFakeUser(String id, Role role) {
        var emailId = Objects.equals(id, "1") ? "" : id;
        var user = new User(
                id,
                "already" + emailId + "@example.fr",
                "Azerty123456789@", null);

        if (role == Role.ADMIN) {
            user.grant();
        }
        user.resetPassword(passwordHasher.hash("Azerty123456789@"));
        user.createVerificationCode(15);
        userRepository.save(user);
        return user;
    }

    protected Device createFakeDevice(String id) {
        var device = new Device(
                id,
                new SerialNumber("1234567890"),
                new Pin("1234567890"),
                null,
                "Lulu"
        );
        deviceRepository.save(device);
        return device;
    }

    protected Device createFakeDevice(String id, String userId) {
        var user = userRepository.findById(userId).get();
        var device = new Device(
                id,
                new SerialNumber("1234567890"),
                new Pin("1234567890"),
                user,
                "Lulu"
        );
        user.linkDevice(device);
        userRepository.save(user);
        deviceRepository.save(device);
        return device;
    }


    protected void activateUser() {
        var user = userRepository.findById("1").get();
        user.makeActive();
        userRepository.save(user);
    }

    protected void expiredVerificationCode() {
        var user = userRepository.findById("1").get();
        user.updateVerificationCodeExpirationDate(-15);
        userRepository.save(user);
    }

    protected LoggedInUserViewModel createAccessAndRefreshToken() {
        var user = userRepository.findByEmailAddress("already@example.fr").orElse(null);
        if (user == null) {
            user = new User(
                    UUID.randomUUID().toString(),
                    "already@example.fr",
                    "Azerty123456789@", null);
            user.resetPassword(passwordHasher.hash("Azerty123456789@"));
            user.createVerificationCode(15);
            userRepository.save(user);
        }
        return new LoggedInUserViewModel(null, null, jwtService.generateAccessToken(user),
                jwtService.generateRefreshToken(user), Role.VISITOR.toString());
    }

    protected String createJwt() {
        var user = userRepository.findById("1").orElse(null);
        if (user == null) {
            user = new User("1", "already@example.fr", "", null);
            userRepository.save(user);
        }
        return "Bearer " + jwtService.generateAccessToken(user);
    }

    protected String createJwt(String userId) {
        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            user = new User(userId, "already@example.fr", "", null);
            userRepository.save(user);
        }
        return "Bearer " + jwtService.generateAccessToken(user);
    }
}
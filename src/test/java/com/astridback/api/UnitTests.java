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
import com.astridback.api.infrastructure.services.ConcreteJwtService;
import com.astridback.api.core.application.ports.APILogger;
import com.astridback.api.core.application.ports.Mailer;
import com.astridback.api.core.infrastructure.spring.SpringLogger;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@Import(PostgresSQLTestConfiguration.class)
@ExtendWith(MockitoExtension.class)
public class UnitTests {
    @Mock
    protected Mailer mailer;

    protected JwtService jwtService = new ConcreteJwtService(
            "fake-secret-access_secretfake-secret-access_secret",
            10,
            "fake-secret-access_secretfake-secret-access_secret",
            10);

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected DeviceRepository deviceRepository;

    @Autowired
    protected PositionRepository positionRepository;

    protected PasswordHasher passwordHasher = new BcryptPasswordHasher();
    protected final APILogger apiLogger = new SpringLogger();

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

    protected User createFakeUser(String id, Role role) {
        var emailId = Objects.equals(id, "1") ? "" : id;
        var user = new User(
                id,
                "already" + emailId + "@example.fr",
                "Azerty123456789@", null);

        if (Role.ADMIN == role) {
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
}

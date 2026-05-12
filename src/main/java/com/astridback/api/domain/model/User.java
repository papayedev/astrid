package com.astridback.api.domain.model;

import com.astridback.api.domain.valueobject.EmailAddress;
import com.astridback.api.domain.valueobject.Password;
import com.astridback.api.domain.valueobject.Role;
import com.astridback.api.domain.valueobject.VerificationCode;

import java.time.LocalDateTime;

public class User {
    private String id;
    private EmailAddress emailAddress;
    private Password password;
    private VerificationCode verificationCode;
    private LocalDateTime verificationCodeExpirationDate;
    private Role role;
    private Device device;
    private boolean active;

    private User() {
    }

    public User(String id, String emailAddress, String passwordValue, Device device) {
        this.id = id;
        this.emailAddress = new EmailAddress(emailAddress);
        if (passwordValue != null) {
            this.password = new Password(passwordValue, null);
        }
        this.role = Role.VISITOR;
        this.device = device;
        this.active = false;
    }

    public static User fromPersistence(String id, String emailAddress, String passwordHash,
            String verificationCode, LocalDateTime verificationCodeExpirationDate,
            boolean active, Role role, Device device) {
        final User user = new User();
        user.id = id;
        user.role = role;
        user.emailAddress = new EmailAddress(emailAddress);
        user.password = new Password(null, passwordHash);
        if (verificationCode != null) {
            user.verificationCode = new VerificationCode(verificationCode);
        }
        user.verificationCodeExpirationDate = verificationCodeExpirationDate;
        user.active = active;
        user.device = device;
        return user;
    }

    public boolean hasAlreadyDevice() {
        return device != null;
    }

    public boolean isActive() {
        return active;
    }

    public Role getRole() {
        return role;
    }

    public boolean isVisitor() {
        return Role.VISITOR.equals(role);
    }

    public boolean isAdmin() {
        return Role.ADMIN.equals(role);
    }

    public void makeActive() {
        this.active = true;
    }

    public void makeInactive() {
        this.active = false;
    }

    public void createVerificationCode(int minutesExpiration) {
        this.verificationCode = new VerificationCode();
        this.updateVerificationCodeExpirationDate(minutesExpiration);
    }

    public void updateVerificationCodeExpirationDate(int minutes) {
        this.verificationCodeExpirationDate = LocalDateTime.now().plusMinutes(minutes);
    }

    public Device getDevice() {
        return device;
    }

    public void linkDevice(Device device) {
        this.device = device;
    }

    public void grant() {
        this.role = Role.ADMIN;
    }

    public void resetVerificationCode() {
        this.verificationCode = null;
    }

    public void clearVerificationCode() {
        this.verificationCode = null;
        this.verificationCodeExpirationDate = null;
    }

    public void resetPassword(String newHashedPassword) {
        this.password = new Password(null, newHashedPassword);
        clearVerificationCode();
    }

    public boolean isVerificationCodeExpired() {
        return verificationCodeExpirationDate == null
                || LocalDateTime.now().isAfter(verificationCodeExpirationDate);
    }

    public String getEmailAddress() {
        return emailAddress.getValue();
    }

    public LocalDateTime getVerificationCodeExpirationDate() {
        return verificationCodeExpirationDate;
    }

    public String getVerificationCode() {
        return verificationCode == null ? null : verificationCode.getValue();
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password.getHashedPassword();
    }

}
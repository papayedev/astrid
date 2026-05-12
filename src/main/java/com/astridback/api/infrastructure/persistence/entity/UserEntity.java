package com.astridback.api.infrastructure.persistence.entity;

import com.astridback.api.core.infrastructure.persistence.entity.BaseEntity;
import com.astridback.api.domain.valueobject.Role;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(name = "email_address", nullable = false, unique = true)
    private String emailAddress;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "verification_code", length = 5)
    private String verificationCode;

    @Column(name = "verification_code_expires_at")
    private LocalDateTime verificationCodeExpirationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "active", nullable = false)
    private boolean active;

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private DeviceEntity device;

    public UserEntity() {
    }

    public UserEntity(String id, String emailAddress, String password) {
        super(id);
        this.emailAddress = emailAddress;
        this.passwordHash = password;
        this.active = false;
        this.role = Role.VISITOR;
        this.verificationCode = null;
    }

    public DeviceEntity getDevice() {
        return device;
    }

    public void setDevice(DeviceEntity device) {
        this.device = device;
    }

    public boolean isActive() {
        return active;
    }

    public Role getRole() { return role; }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public LocalDateTime getVerificationCodeExpirationDate() {
        return verificationCodeExpirationDate;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setVerificationCodeExpirationDate(LocalDateTime verificationCodeExpirationDate) {
        this.verificationCodeExpirationDate = verificationCodeExpirationDate;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
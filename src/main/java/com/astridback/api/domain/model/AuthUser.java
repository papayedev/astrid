package com.astridback.api.domain.model;

import com.astridback.api.domain.valueobject.Role;

public class AuthUser {
    private String id;
    private String emailAddress;
    private Role role;

    public AuthUser() {
    }

    public AuthUser(String id, String emailAddress, String role) {
        this.id = id;
        this.emailAddress = emailAddress;
        this.role = Role.valueOf(role);
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

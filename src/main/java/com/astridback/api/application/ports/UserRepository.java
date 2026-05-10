package com.astridback.api.application.ports;

import com.astridback.api.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(String id);

    Optional<User> findByEmailAddress(String email);

    void save(User user);

    void clear();
}
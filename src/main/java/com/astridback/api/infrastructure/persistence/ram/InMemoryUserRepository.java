package com.astridback.api.infrastructure.persistence.ram;

import com.astridback.api.application.ports.UserRepository;
import com.astridback.api.domain.model.User;
import com.astridback.api.infrastructure.persistence.entity.UserEntity;
import com.astridback.api.infrastructure.persistence.mapper.UserMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {

    private final Map<String, UserEntity> entities = new HashMap<>();

    @Override
    public Optional<User> findById(String id) {
        return entities.values().stream()
                .filter(user -> Objects.equals(user.getId(), id))
                .findFirst()
                .map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmailAddress(String emailAddress) {
        return entities.values().stream()
                .filter(user -> user.getEmailAddress().equals(emailAddress))
                .findFirst()
                .map(UserMapper::toDomain);
    }

    @Override
    public void save(User user) {
        final UserEntity entity = UserMapper.toEntity(user);
        entities.put(entity.getId(), entity);
    }

    @Override
    public void clear() {
        entities.clear();
    }
}
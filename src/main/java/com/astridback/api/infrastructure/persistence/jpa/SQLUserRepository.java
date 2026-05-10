package com.astridback.api.infrastructure.persistence.jpa;

import com.astridback.api.application.ports.UserRepository;
import com.astridback.api.domain.model.User;
import com.astridback.api.infrastructure.persistence.entity.UserEntity;
import com.astridback.api.infrastructure.persistence.mapper.UserMapper;

import java.util.Optional;

public class SQLUserRepository implements UserRepository {

    private final SQLUserAccessor accessor;

    public SQLUserRepository(SQLUserAccessor accessor) {
        this.accessor = accessor;
    }

    @Override
    public Optional<User> findById(String id) {
        return accessor.findById(id).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmailAddress(String emailAddress) {
        return Optional.ofNullable(accessor.findByEmailAddress(emailAddress))
                .map(UserMapper::toDomain);
    }

    @Override
    public void save(User user) {
        UserEntity entity = UserMapper.toEntity(user);
        accessor.save(entity);
    }

    @Override
    public void clear() {
        accessor.deleteAll();
    }
}
package com.astridback.api.infrastructure.persistence.mapper;

import com.astridback.api.domain.model.User;
import com.astridback.api.infrastructure.persistence.entity.UserEntity;

public class UserMapper {

    public static User toDomain(UserEntity entity) {
        if (entity == null) return null;

        var user = User.fromPersistence(
                entity.getId(),
                entity.getEmailAddress(),
                entity.getPasswordHash(),
                entity.getVerificationCode(),
                entity.getVerificationCodeExpirationDate(),
                entity.isActive(),
                entity.getRole(),
                null
        );

        if (entity.getDevice() != null) {
            var device = DeviceMapper.toDomain(entity.getDevice());
            user.linkDevice(device);
        }

        return user;
    }

    public static UserEntity toEntity(User user) {
        var entity = new UserEntity();
        entity.setId(user.getId());
        entity.setEmailAddress(user.getEmailAddress());
        entity.setPasswordHash(user.getPassword());
        entity.setVerificationCode(user.getVerificationCode());
        entity.setVerificationCodeExpirationDate(user.getVerificationCodeExpirationDate());
        entity.setActive(user.isActive());
        entity.setRole(user.getRole());

        if (user.getDevice() != null) {
            var deviceEntity = DeviceMapper.toEntity(user.getDevice());
            deviceEntity.setUser(entity);
            entity.setDevice(deviceEntity);
        }

        return entity;
    }
}
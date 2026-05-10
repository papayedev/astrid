package com.astridback.api.infrastructure.persistence.mapper;

import com.astridback.api.domain.model.User;
import com.astridback.api.infrastructure.persistence.entity.UserEntity;

public class UserMapper {
    public static User toDomain(UserEntity entity) {
        return User.fromPersistence(
                entity.getId(),
                entity.getEmailAddress(),
                entity.getPasswordHash(),
                entity.getVerificationCode(),
                entity.getVerificationCodeExpirationDate(),
                entity.isActive(),
                entity.getRole(),
                entity.getSerialPin() != null
                        ? DeviceMapper.toDomain(entity.getSerialPin())
                        : null
        );
    }
    public static UserEntity toEntity(User user) {
        var entity = new UserEntity();
        entity.setId(user.getId());
        entity.setVerificationCodeExpirationDate(user.getVerificationCodeExpirationDate());
        entity.setVerificationCode(user.getVerificationCode());
        entity.setActive(user.isActive());
        entity.setEmailAddress(user.getEmailAddress());
        entity.setPasswordHash(user.getPassword());
        entity.setRole(user.getRole());

        if (user.getDevice() != null) {
            var serialPinEntity = DeviceMapper.toEntity(user.getDevice());
            serialPinEntity.setUser(entity);
            entity.setSerialPin(serialPinEntity);
        }

        return entity;
    }
}

    package com.astridback.api.infrastructure.persistence.mapper;

    import com.astridback.api.domain.model.Device;
    import com.astridback.api.domain.model.Position;
    import com.astridback.api.domain.model.User;
    import com.astridback.api.infrastructure.persistence.entity.DeviceEntity;
    import com.astridback.api.infrastructure.persistence.entity.PositionEntity;
    import com.astridback.api.infrastructure.persistence.entity.UserEntity;

    public class DeviceMapper {

        public static Device toDomain(DeviceEntity entity) {
            if (entity == null) return null;

            User user = null;
            if (entity.getUser() != null) {
                var u = entity.getUser();
                user = User.fromPersistence(
                        u.getId(),
                        u.getEmailAddress(),
                        u.getPasswordHash(),
                        u.getVerificationCode(),
                        u.getVerificationCodeExpirationDate(),
                        u.isActive(),
                        u.getRole(),
                        null
                );
            }

            return Device.fromPersistence(
                    entity.getId(),
                    entity.getName(),
                    entity.getSerialNumber(),
                    entity.getPin(),
                    user,
                    PositionMapper.toDomain(entity.getPositions())
             );
        }

        public static DeviceEntity toEntity(Device device) {
            if (device == null) return null;

            var entity = new DeviceEntity();
            entity.setId(device.getId());
            entity.setName(device.getName());
            entity.setSerialNumber(device.getSerialNumber().getValue());
            entity.setPin(device.getSerialPin().getValue());
            entity.setPositions(PositionMapper.toEntity(device.getPositions()));

            if (device.getUser() != null) {
                var userEntity = new UserEntity();
                userEntity.setId(device.getUser().getId());
                entity.setUser(userEntity);
            }

            return entity;
        }
    }
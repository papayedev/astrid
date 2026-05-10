package com.astridback.api.infrastructure.persistence.mapper;

import com.astridback.api.domain.model.Device;
import com.astridback.api.infrastructure.persistence.entity.DeviceEntity;

public class DeviceMapper {
    public static Device toDomain(DeviceEntity entity) {
        if (entity == null) return null;
        return Device.fromPersistence(
                entity.getId(),
                entity.getName(),
                entity.getSerialNumber(),
                entity.getPin(),
                null
        );
    }

    public static DeviceEntity toEntity(Device device) {
        if (device == null) return null;
        var entity = new DeviceEntity();
        entity.setId(device.getId());
        entity.setPin(device.getSerialPin().getValue());
        entity.setSerialNumber(device.getSerialNumber().getValue());
        entity.setName(device.getName());
        return entity;
    }
}

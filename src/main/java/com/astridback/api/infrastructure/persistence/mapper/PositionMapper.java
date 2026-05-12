package com.astridback.api.infrastructure.persistence.mapper;

import com.astridback.api.domain.model.Device;
import com.astridback.api.domain.model.Position;
import com.astridback.api.infrastructure.persistence.entity.DeviceEntity;
import com.astridback.api.infrastructure.persistence.entity.PositionEntity;

import java.util.List;

public class PositionMapper {

    public static List<Position> toDomain(List<PositionEntity> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(position -> Position.fromPersistence(
                        position.getId(),
                        position.getLatitude(),
                        position.getLongitude(),
                        Device.fromPersistence(
                                position.getDevice().getId(),
                                position.getDevice().getName(),
                                position.getDevice().getSerialNumber(),
                                position.getDevice().getPin(),
                                null,
                                null
                        )
                ))
                .toList();
    }

    public static List<PositionEntity> toEntity(List<Position> positions) {
        if (positions == null) {
            return null;
        }

        return positions.stream()
                .map(position -> {
                    PositionEntity entity = new PositionEntity();

                    entity.setId(position.getId());
                    entity.setLatitude(position.getLatitude());
                    entity.setLongitude(position.getLongitude());

                    if (position.getDevice() != null) {
                        DeviceEntity deviceEntity = new DeviceEntity();
                        deviceEntity.setId(position.getDevice().getId());

                        entity.setDevice(deviceEntity);
                    }

                    return entity;
                })
                .toList();
    }
}
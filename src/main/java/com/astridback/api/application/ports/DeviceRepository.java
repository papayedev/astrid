package com.astridback.api.application.ports;

import com.astridback.api.domain.model.Device;
import com.astridback.api.domain.valueobject.DevicePage;

import java.util.Optional;

public interface DeviceRepository {
    Optional<Device> findBySerialNumber(String serialNumber);
    Optional<Device> findByName(String name);
    Optional<Device> findById(String id);
    Optional<Device> findByUserId(String userId);
    Optional<DevicePage> findAll(int pageNumber);
    void deleteById(String id);
    void save(Device device);
    void clear();
}

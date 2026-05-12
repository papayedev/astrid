package com.astridback.api.infrastructure.persistence.jpa;

import com.astridback.api.application.ports.DeviceRepository;
import com.astridback.api.domain.model.Device;
import com.astridback.api.domain.valueobject.DevicePage;
import com.astridback.api.infrastructure.persistence.mapper.DeviceMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Transactional
public class SQLDeviceRepository implements DeviceRepository {
    private SQLDeviceAccessor accessor;

    public SQLDeviceRepository(SQLDeviceAccessor accessor) {
        this.accessor = accessor;
    }

    @Override
    public Optional<Device> findBySerialNumber(String serialNumber) {
        return accessor.findFirstBySerialNumber(serialNumber)
                .map(DeviceMapper::toDomain);
    }

    @Override
    public Optional<Device> findByName(String name) {
        return accessor.findFirstByName(name)
                .map(DeviceMapper::toDomain);
    }

    @Override
    public Optional<Device> findById(String id) {
        return accessor.findById(id)
                .map(DeviceMapper::toDomain);
    }

    @Override
    public Optional<Device> findByUserId(String userId) {
        return accessor.findFirstByUserId(userId)
                .map(DeviceMapper::toDomain);
    }

    @Override
    public Optional<DevicePage> findAll(int pageNumber) {
        var page = accessor.findAll(PageRequest.of(pageNumber, 5));
        return Optional.of(new DevicePage(
                page.getContent().stream().map(DeviceMapper::toDomain).toList(),
                String.valueOf(page.getTotalPages())
        ));
    }

    @Override
    public void deleteById(String id) {
        accessor.deleteDeviceById(id);
    }

    @Override
    public void save(Device device) {
        accessor.save(DeviceMapper.toEntity(device));
    }

    @Override
    public void clear() {
        accessor.deleteAll();
    }
}

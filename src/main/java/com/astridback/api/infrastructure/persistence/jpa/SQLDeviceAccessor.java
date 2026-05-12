package com.astridback.api.infrastructure.persistence.jpa;

import com.astridback.api.infrastructure.persistence.entity.DeviceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SQLDeviceAccessor extends CrudRepository<DeviceEntity, String> {
    Optional<DeviceEntity> findFirstBySerialNumber(String serialNumber);
    Optional<DeviceEntity> findFirstByName(String name);
    Optional<DeviceEntity> findFirstByUserId(String userId);
    Page<DeviceEntity> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM DeviceEntity d WHERE d.id = :id")
    void deleteDeviceById(String id);
}

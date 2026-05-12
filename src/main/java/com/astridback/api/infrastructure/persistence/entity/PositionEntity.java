package com.astridback.api.infrastructure.persistence.entity;

import com.astridback.api.core.infrastructure.persistence.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "positions")
public class PositionEntity extends BaseEntity {
    @Column(name = "latitude", nullable = false)
    private String latitude;

    @Column(name = "longitude", nullable = false)
    private String longitude;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "device_id")
    private DeviceEntity device;

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public DeviceEntity getDevice() {
        return device;
    }

    public void setDevice(DeviceEntity device) {
        this.device = device;
    }
}

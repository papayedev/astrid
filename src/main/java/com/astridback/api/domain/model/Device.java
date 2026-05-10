package com.astridback.api.domain.model;

import com.astridback.api.domain.valueobject.Pin;
import com.astridback.api.domain.valueobject.SerialNumber;

public class Device {
    private String id;
    private String name;
    private SerialNumber  serialNumber;
    private Pin serialPin;
    private User user;

    public Device() {
    }

    public Device(String id, SerialNumber serialNumber, Pin serialPin, User user, String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name is null");
        }
        if (name.length() > 10) {
            throw new IllegalArgumentException("Name is too long");
        }
        this.id = id;
        this.name = name;
        this.serialNumber = serialNumber;
        this.serialPin = serialPin;
        this.user = user;
    }

    public static Device fromPersistence(String id, String name, String serialNumber, String pin, User user) {
        var device = new Device();
        device.id = id;
        device.name = name;
        device.serialNumber = new SerialNumber(serialNumber);
        device.serialPin = new  Pin(pin);
        device.user = user;
        return device;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Pin getSerialPin() {
        return serialPin;
    }

    public User getUser() {
        return user;
    }

    public SerialNumber getSerialNumber() {
        return serialNumber;
    }
}

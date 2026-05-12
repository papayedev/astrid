package com.astridback.api.domain.model;

import com.astridback.api.domain.valueobject.Pin;
import com.astridback.api.domain.valueobject.SerialNumber;

import java.util.ArrayList;
import java.util.List;

public class Device {
    private String id;
    private String name;
    private SerialNumber  serialNumber;
    private Pin serialPin;
    private User user;
    private List<Position> positions;

    public Device() {
    }

    public Device(String id, SerialNumber serialNumber, Pin serialPin, User user, String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name is empty");
        }
        if (name.length() > 10) {
            throw new IllegalArgumentException("Name is too long");
        }
        this.id = id;
        this.name = name;
        this.serialNumber = serialNumber;
        this.serialPin = serialPin;
        this.user = user;
        this.positions = new ArrayList<>();
    }

    public static Device fromPersistence(String id, String name, String serialNumber, String pin, User user, List<Position> positions) {
        var device = new Device();
        device.id = id;
        device.name = name;
        device.serialNumber = new SerialNumber(serialNumber);
        device.serialPin = new  Pin(pin);
        device.user = user;
        device.positions = positions != null ? new ArrayList<>(positions) : new ArrayList<>();
        return device;
    }

    public boolean hasUser() {
        return user != null;
    }

    public boolean matchPin(String pin) {
        return pin.equals(this.serialPin.getValue());
    }

    public String getId() {
        return id;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
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

    public void addPosition(String id, String latitude, String longitude) {
        this.positions.add(new Position(
                id,
                latitude,
                longitude,
                this
        ));
    }
}

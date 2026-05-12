package com.astridback.api.domain.model;

public class Position {
    private String id;
    private String longitude;
    private String latitude;
    private Device device;

    public Position(String id, String longitude, String latitude, Device device) {
        if (id.isEmpty() || longitude.isEmpty() || latitude.isEmpty() || device == null) {
            throw new IllegalArgumentException("Fields empty");
        }
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.device = device;
    }

    public Position() {}

    public static Position fromPersistence(String id, String latitude, String longitude, Device device) {
        var position = new Position();
        position.id = id;
        position.longitude = longitude;
        position.latitude = latitude;
        position.device = device;
        return position;
    }

    public String getId() {
        return id;
    }

    public Device getDevice() {
        return device;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}

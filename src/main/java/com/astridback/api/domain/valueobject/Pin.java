package com.astridback.api.domain.valueobject;

public class Pin {
    private final String value;

    public Pin(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Pin cannot be null");
        }
        if (value.length() != 10) {
            throw new IllegalArgumentException("Pin cannot contain more than 10 characters");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

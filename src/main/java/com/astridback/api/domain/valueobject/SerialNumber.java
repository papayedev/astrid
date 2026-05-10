package com.astridback.api.domain.valueobject;

public class SerialNumber {
    private final String value;

    public SerialNumber(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Serial Number cannot be null");
        }
        if (value.length() != 10) {
            throw new IllegalArgumentException("Serial Number cannot contain more than 10 characters");
        }
        if (!value.matches("^\\d+$")) {
            throw new IllegalArgumentException("Serial Number can only contain numbers");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

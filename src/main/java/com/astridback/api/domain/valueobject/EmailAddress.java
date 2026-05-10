package com.astridback.api.domain.valueobject;

import java.util.regex.Pattern;

public class EmailAddress {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    private String value;

    public EmailAddress(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("The email address cannot be blank");
        }

        final String trimmedValue = value.trim().toLowerCase();

        if (!EMAIL_PATTERN.matcher(trimmedValue).matches()) {
            throw new IllegalArgumentException("The email address is not valid");
        }

        this.value = trimmedValue;
    }

    public String getValue() {
        return value;
    }
}

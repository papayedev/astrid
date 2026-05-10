package com.astridback.api.domain.valueobject;

import java.security.SecureRandom;

public class VerificationCode {
    private static final String CHARACTERS = "0123456789";
    private static final int CODE_LENGTH = 5;
    private static final SecureRandom random = new SecureRandom();

    private String value;

    private String generateCode() {
        final StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }

    public VerificationCode(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("The verification code cannot be blank");
        }

        final String trimmedValue = value.trim();

        if (trimmedValue.length() > CODE_LENGTH) {
            throw new IllegalArgumentException(
                    "The verification code cannot contain more than " + CODE_LENGTH + " characters");
        }

        this.value = trimmedValue;
    }

    public VerificationCode() {
        this.value = generateCode();
    }

    public String getValue() {
        return value;
    }
}

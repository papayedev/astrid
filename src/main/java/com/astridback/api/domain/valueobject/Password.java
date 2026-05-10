package com.astridback.api.domain.valueobject;

import java.util.regex.Pattern;

public class Password {
    private static final int MIN_LENGTH = 8;
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");

    private String clearPassword;
    private String hashedPassword;

    public Password(String clearPassword, String hashedPassword) {
        if (clearPassword != null && !clearPassword.isBlank()) {
            validateClearPassword(clearPassword);
            this.clearPassword = clearPassword;
        }

        this.hashedPassword = hashedPassword;
    }

    private void validateClearPassword(String clearPassword) {
        if (clearPassword.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("The password can contain min " + MIN_LENGTH + " characters");
        }

        if (!UPPERCASE_PATTERN.matcher(clearPassword).find()) {
            throw new IllegalArgumentException("The password must contain at least one uppercase letter");
        }

        if (!LOWERCASE_PATTERN.matcher(clearPassword).find()) {
            throw new IllegalArgumentException("The password must contain at least one lowercase letter");
        }

        if (!DIGIT_PATTERN.matcher(clearPassword).find()) {
            throw new IllegalArgumentException("The password must contain at least one digit");
        }

        if (!SPECIAL_CHAR_PATTERN.matcher(clearPassword).find()) {
            throw new IllegalArgumentException(
                    "The password must contain at least one special character: !@#$%^&*(),.?\":{}|<>");
        }
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getClearPassword() {
        return clearPassword;
    }
}

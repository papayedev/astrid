package com.astridback.api.presentation.dto;

public record UpdatePasswordDTO(
        String email,
        String verificationCode,
        String password
) {

}

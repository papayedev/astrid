package com.astridback.api.presentation.dto;

public record CreateDeviceDTO(
        String name,
        String serialNumber,
        String pin
) {

}

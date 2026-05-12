package com.astridback.api.domain.viewmodel;

public record DeviceResponse (
        String id,
        String name,
        String serialNumber,
        String pin,
        String userId
){
}

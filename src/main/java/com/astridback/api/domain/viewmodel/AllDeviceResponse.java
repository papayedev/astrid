package com.astridback.api.domain.viewmodel;

import java.util.List;

public record AllDeviceResponse(
        List<DeviceResponse> devices,
        String maxPage
) {
}

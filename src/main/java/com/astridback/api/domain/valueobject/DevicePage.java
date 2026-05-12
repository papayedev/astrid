package com.astridback.api.domain.valueobject;

import com.astridback.api.domain.model.Device;

import java.util.List;

public class DevicePage {
    private List<Device> devices;
    private String maxPage;

    public DevicePage(List<Device> devices, String maxPage) {
        this.devices = devices;
        this.maxPage = maxPage;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public String getMaxPage() {
        return maxPage;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public void setMaxPage(String maxPage) {
        this.maxPage = maxPage;
    }
}

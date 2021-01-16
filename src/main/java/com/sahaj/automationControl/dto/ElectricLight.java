package com.sahaj.automationControl.dto;

import com.sahaj.automationControl.enums.DeviceType;

/**
 * Represents a device, electric light
 */
public class ElectricLight extends com.sahaj.automationControl.dto.Device {

    public ElectricLight(int id) {
        super(id);
    }

    @Override
    public DeviceType deviceType() {
        return DeviceType.ELECTRIC_LIGHT;
    }

}

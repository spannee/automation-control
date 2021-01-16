package com.sahaj.automationControl.dto;

import com.sahaj.automationControl.enums.DeviceType;

/**
 * Represents a device, air conditioner
 */
public class AirConditioner extends com.sahaj.automationControl.dto.Device {

    public AirConditioner(int id) {
        super(id);
    }

    @Override
    public DeviceType deviceType() {
        return DeviceType.AIR_CONDITIONER;
    }

}

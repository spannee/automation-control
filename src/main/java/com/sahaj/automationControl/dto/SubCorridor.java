package com.sahaj.automationControl.dto;

import com.sahaj.automationControl.enums.DeviceType;
import com.sahaj.automationControl.enums.Status;

/**
 * Represents a corridor type, sub corridor
 */
public class SubCorridor extends Corridor {

    public SubCorridor(int id, Device electricLight, Device airConditioner) {
        super(id, electricLight, airConditioner);
    }

    @Override
    Status getDefaultState(DeviceType deviceType) {
        if (deviceType == DeviceType.AIR_CONDITIONER)
            return Status.ON;
        return Status.OFF;
    }

}
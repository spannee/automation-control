package com.sahaj.automationControl.dto;

import com.sahaj.automationControl.enums.DeviceType;
import com.sahaj.automationControl.enums.Status;
import com.sahaj.automationControl.utils.HotelMotionSensorUtils;

/**
 * Represents a corridor type, main corridor
 */
public class MainCorridor extends Corridor {

    public MainCorridor(int id, Device electricLight, Device airConditioner) {
        super(id, electricLight, airConditioner);
    }

    @Override
    Status getDefaultState(DeviceType deviceType) {
        if ((deviceType == DeviceType.ELECTRIC_LIGHT &&
                HotelMotionSensorUtils.isNightTime()) ||
            (deviceType == DeviceType.AIR_CONDITIONER)) {
            return Status.ON;
        }
        return Status.OFF;
    }

}

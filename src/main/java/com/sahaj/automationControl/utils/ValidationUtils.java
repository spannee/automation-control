package com.sahaj.automationControl.utils;

import com.sahaj.automationControl.enums.CorridorType;
import com.sahaj.automationControl.enums.ErrorMessages;
import com.sahaj.automationControl.exception.AutomationControlException;
import com.sahaj.automationControl.plan.HotelConsumptionPlan;

public class ValidationUtils {

    /**
     * A method to validate hotel sensor data
     * @param floor
     * @param corridor
     * @param corridorType
     * @throws AutomationControlException
     */
    public static void validateSensorData(int floor, int corridor, CorridorType corridorType) throws AutomationControlException {
        if (floor > HotelConsumptionPlan.getFloorCount())
            throw new AutomationControlException(ErrorMessages.INVALID_FLOOR_NUMBER.getErrorMessages());
        if (corridorType == CorridorType.MAIN_CORRIDOR && corridor > HotelConsumptionPlan.getMainCorridorCount())
            throw new AutomationControlException(ErrorMessages.INVALID_MC_ID.getErrorMessages());
        if (corridorType == CorridorType.SUB_CORRIDOR && corridor > HotelConsumptionPlan.getSubCorridorCount())
            throw new AutomationControlException(ErrorMessages.INVALID_SC_ID.getErrorMessages());
    }
}

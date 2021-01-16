package com.sahaj.automationControl.plan;

import com.sahaj.automationControl.dto.Device;
import com.sahaj.automationControl.dto.Floor;
import com.sahaj.automationControl.enums.CorridorType;
import com.sahaj.automationControl.enums.DeviceType;
import com.sahaj.automationControl.enums.Status;
import com.sahaj.automationControl.exception.AutomationControlException;

import java.util.Map;

public interface ConsumptionPlan {

    /**
     * Consumption plan when there is movement in corridors
     * @param floors
     * @param floor
     * @param corridor
     * @param corridorType
     * @throws AutomationControlException
     */
    void movement(Map<Integer, Floor> floors, int floor, int corridor, CorridorType corridorType) throws AutomationControlException;

    /**
     * Consumption plan when there is no movement in corridors
     * @param floors
     * @param floor
     * @param corridor
     * @param corridorType
     * @throws AutomationControlException
     */
    void noMovement(Map<Integer, Floor> floors, int floor, int corridor, CorridorType corridorType) throws AutomationControlException;

    /**
     * Toggle devices
     * @param floors
     * @param floor
     * @param corridor
     * @param status
     * @param deviceType
     */
    void switchDeviceStatus(Map<Integer, Floor> floors, int floor, int corridor, Status status, DeviceType deviceType);

    /**
     * Calculate power consumed
     * @param floor
     * @param device
     * @param previousStatus
     * @throws AutomationControlException
     */
    void powerConsumed(int floor, Device device, Status previousStatus) throws AutomationControlException;

    /**
     * To check if power has exceeded the limit
     * @param floor
     * @return
     */
    boolean hasTotalPowerBeenConsumed(int floor);

}

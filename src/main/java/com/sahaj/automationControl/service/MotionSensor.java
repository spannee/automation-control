package com.sahaj.automationControl.service;

import com.sahaj.automationControl.enums.CorridorType;
import com.sahaj.automationControl.exception.AutomationControlException;
import com.sahaj.automationControl.plan.ConsumptionPlan;

public interface MotionSensor {

    /**
     * Controls devices and calculates power when there is movement in the corridor
     * based on the consumption plan
     * @param floor
     * @param corridor
     * @param corridorType
     * @param consumptionPlan
     * @throws AutomationControlException
     */
    void movement(int floor, int corridor, CorridorType corridorType, ConsumptionPlan consumptionPlan) throws AutomationControlException;

    /**
     * Controls devices and calculates power when there is no movement in the corridor
     * for more than a minute based on the consumption plan
     * @param floor
     * @param corridor
     * @param corridorType
     * @param consumptionPlan
     * @throws AutomationControlException
     */
    void noMovement(int floor, int corridor, CorridorType corridorType, ConsumptionPlan consumptionPlan) throws AutomationControlException;

    /**
     * Destroys the instance at the end
     */
    void destroy();

}

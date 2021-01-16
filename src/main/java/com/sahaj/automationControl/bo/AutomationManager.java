package com.sahaj.automationControl.bo;

import com.sahaj.automationControl.enums.CorridorType;
import com.sahaj.automationControl.exception.AutomationControlException;

public interface AutomationManager {

    /**
     * Initialize the default state of electrical devices
     * @param floorCount
     * @param mainCorridorCount
     * @param subCorridorCount
     */
    void defaultState(int floorCount, int mainCorridorCount, int subCorridorCount);

    /**
     * Controls devices and calculates power when there is movement in the corridor
     * @param floor
     * @param corridor
     * @param corridorType
     * @throws AutomationControlException
     */
    void movement(int floor, int corridor, CorridorType corridorType) throws AutomationControlException;

    /**
     * Controls devices and calculates power when there is no movement in the corridor
     * for more than a minute
     * @param floor
     * @param corridor
     * @param corridorType
     * @throws AutomationControlException
     */
    void noMovement(int floor, int corridor, CorridorType corridorType) throws AutomationControlException;

    /**
     * Destroys the instance at the end
     */
    void destroy();

}

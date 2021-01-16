package com.sahaj.automationControl.executor;

import com.sahaj.automationControl.bo.AutomationManager;
import com.sahaj.automationControl.enums.CorridorType;
import com.sahaj.automationControl.enums.InputType;
import com.sahaj.automationControl.exception.AutomationControlException;

public interface Controller {

    /**
     * Sets the manager which is required
     * @param automationManager
     */
    void setManager(AutomationManager automationManager);

    /**
     * Processes the inputs based on input type
     * @param inputType
     * @param sensorMovementParams
     * @param countParams
     * @param corridorType
     * @throws AutomationControlException
     */
    void process(InputType inputType, int[] sensorMovementParams, int countParams[], CorridorType corridorType) throws AutomationControlException;

    /**
     * Destroys the instance at the end
     */
    void destroyManagerInstance();

}

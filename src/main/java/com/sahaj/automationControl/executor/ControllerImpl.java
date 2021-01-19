package com.sahaj.automationControl.executor;

import com.sahaj.automationControl.bo.AutomationManager;
import com.sahaj.automationControl.enums.CorridorType;
import com.sahaj.automationControl.enums.ErrorMessages;
import com.sahaj.automationControl.enums.InputType;
import com.sahaj.automationControl.exception.AutomationControlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerImpl implements Controller {

    private static final Logger logger = LoggerFactory.getLogger("ControllerImpl");

    private AutomationManager automationManager;

    /**
     * Sets the manager which is required
     * @param automationManager
     */
    @Override
    public void setManager(AutomationManager automationManager) {
        this.automationManager = automationManager;
    }

    /**
     * Processes the inputs based on input type
     * @param inputType
     * @param sensorMovementParams
     * @param countParams
     * @param corridorType
     * @throws AutomationControlException
     */
    @Override
    public void process(InputType inputType, int[] sensorMovementParams, int countParams[], CorridorType corridorType) throws AutomationControlException {
        if (inputType == null)
            throw new AutomationControlException(ErrorMessages.INVALID_INPUT.getErrorMessages());

        switch (inputType) {
            case DEFAULT:
                logger.info("Input: The default state (when the program is first to run)");
                automationManager.defaultState(countParams[0], countParams[1], countParams[2]);
                break;
            case MOVEMENT:
                logger.info("Input: Movement in Floor " + sensorMovementParams[0] + ", Sub Corridor " + sensorMovementParams[1]);
                automationManager.movement(sensorMovementParams[0], sensorMovementParams[1], corridorType);
                break;
            case NO_MOVEMENT:
                logger.info("Input: No movement in Floor " + sensorMovementParams[0] + ", Sub Corridor " + sensorMovementParams[1] + " for a minute");
                automationManager.noMovement(sensorMovementParams[0], sensorMovementParams[1], corridorType);
                break;
            default:
                break;
        }
    }

    /**
     * Destroys the instance at the end
     */
    @Override
    public void destroyManagerInstance() {
        automationManager.destroy();
    }
}

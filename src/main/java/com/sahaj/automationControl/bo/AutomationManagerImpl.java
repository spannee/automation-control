package com.sahaj.automationControl.bo;

import com.sahaj.automationControl.enums.CorridorType;
import com.sahaj.automationControl.enums.ErrorMessages;
import com.sahaj.automationControl.exception.AutomationControlException;
import com.sahaj.automationControl.plan.HotelConsumptionPlan;
import com.sahaj.automationControl.service.HotelMotionSensorImpl;
import com.sahaj.automationControl.service.MotionSensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutomationManagerImpl implements AutomationManager {

    private static final Logger logger = LoggerFactory.getLogger("AutomationManagerImpl");

    private MotionSensor motionSensor;

    /**
     * Initialize the default state of electrical devices
     * @param floorCount
     * @param mainCorridorCount
     * @param subCorridorCount
     */
    @Override
    public void defaultState(int floorCount, int mainCorridorCount, int subCorridorCount) {
        motionSensor = HotelMotionSensorImpl.getInstance(floorCount, mainCorridorCount, subCorridorCount);
    }

    /**
     * Controls devices and calculates power when there is movement in the corridor
     * @param floor
     * @param corridor
     * @param corridorType
     * @throws AutomationControlException
     */
    @Override
    public void movement(int floor, int corridor, CorridorType corridorType) throws AutomationControlException {
        if (motionSensor == null) {
            logger.error(ErrorMessages.AUTOMATION_CONTROL_NOT_EXISTS.getErrorMessages());
            return;
        }

        motionSensor.movement(floor, corridor, corridorType, new HotelConsumptionPlan());
    }

    /**
     * Controls devices and calculates power when there is no movement in the corridor
     * for more than a minute
     * @param floor
     * @param corridor
     * @param corridorType
     * @throws AutomationControlException
     */
    @Override
    public void noMovement(int floor, int corridor, CorridorType corridorType) throws AutomationControlException {
        if (motionSensor == null) {
            logger.error(ErrorMessages.AUTOMATION_CONTROL_NOT_EXISTS.getErrorMessages());
            return;
        }

        motionSensor.noMovement(floor, corridor, corridorType, new HotelConsumptionPlan());
    }

    /**
     * Destroys the instance at the end
     */
    @Override
    public void destroy() {
        motionSensor.destroy();
    }

}

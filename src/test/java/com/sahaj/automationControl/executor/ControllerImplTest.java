package com.sahaj.automationControl.executor;

import com.sahaj.automationControl.bo.AutomationManagerImpl;
import com.sahaj.automationControl.dto.Corridor;
import com.sahaj.automationControl.enums.CorridorType;
import com.sahaj.automationControl.enums.InputType;
import com.sahaj.automationControl.enums.Status;
import com.sahaj.automationControl.exception.AutomationControlException;
import com.sahaj.automationControl.plan.HotelConsumptionPlan;
import com.sahaj.automationControl.service.HotelMotionSensorImpl;
import com.sahaj.automationControl.utils.HotelMotionSensorUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

/**
 * How input works in this APP
 *
 * 1. Define input type enum
 * For initial default state, use InputType.DEFAULT
 * For movement, use InputType.MOVEMENT
 * For no movement, use InputType.NO_MOVEMENT
 *
 * 2. Define values for initial state of hotel building
 * countParams array is used to define the initial state of hotel building
 * countParams[0] refers to total number of floors
 * countParams[1] refers to total number of main corridors
 * countParams[2] refers to total number of sub corridors
 * For eg. if you want to define floors as 2, main corridors per floor as 1 and sub corridors per floor as 2,
 * the countParams array would look like {2, 1, 2}
 *
 * 3. Define corridor type
 * When initializing the hotel building, corridor type will be null
 * When there is an action (movement or no_movement), it would be CorridorType.SUB_CORRIDOR
 *
 * 4. Define movement params
 * sensorMovementParams array is used to define inputs when there is an action in the hotel (movement and no_movement)
 * sensorMovementParams[0] refers to floor number
 * sensorMovementParams[1] refers to sub corridor number
 * For eg. if you want to define Movement in Floor 1, Sub corridor 2,
 * the sensorMovementParams array would look like {1, 2}
 *
 * 5. Examples for calling the controller
 * For initializing the building,
 * the call would look like this -> controller.process(InputType.DEFAULT, new int[]{}, countParams, null);
 *
 * For movement in floor 1, sub corridor 2,
 * the call would look like this -> controller.process(InputType.MOVEMENT, sensorMovementParams, countParams, CorridorType.SUB_CORRIDOR);
 *
 * For no movement in Floor 1, sub corridor 2,
 * the call would look like this -> controller.process(InputType.NO_MOVEMENT, sensorMovementParams, countParams, CorridorType.SUB_CORRIDOR);
 */
public class ControllerImplTest {

    Controller controller;

    @Before
    public void init() {
        controller = new ControllerImpl();
        controller.setManager(new AutomationManagerImpl());
    }

    @Test
    public void testInitialStateOfHotel() throws AutomationControlException {
        int[] countParams = new int[] {2, 1, 2};

        controller.process(InputType.DEFAULT, new int[]{}, countParams, null);

        List<Corridor> subCorridors = HotelMotionSensorImpl.getFloors()
                                                           .get(1)
                                                           .getSubCorridor();
        Corridor subCorridor1 = subCorridors.stream()
                                            .filter(sc -> sc.getId() == 1)
                                            .findFirst()
                                            .get();
        Corridor subCorridor2 = subCorridors.stream()
                                            .filter(sc -> sc.getId() == 2)
                                            .findFirst()
                                            .get();
        assertNotNull(subCorridor1);
        assertNotNull(subCorridor2);
        assertEquals(subCorridor1.getAirConditioner().getStatus(), Status.ON);
        assertEquals(subCorridor2.getElectricLight().getStatus(), Status.OFF);
        assertEquals(subCorridor2.getAirConditioner().getStatus(), Status.ON);

        controller.destroyManagerInstance();
    }

    @Test
    public void testMovement() throws AutomationControlException {
        int[] countParams = new int[] {2, 1, 2};
        int[] sensorMovementParams = new int[] {1, 2};

        controller.process(InputType.DEFAULT, new int[]{}, countParams, null);
        controller.process(InputType.MOVEMENT, sensorMovementParams, countParams, CorridorType.SUB_CORRIDOR);

        List<Corridor> subCorridors = HotelMotionSensorImpl.getFloors()
                                                           .get(1)
                                                           .getSubCorridor();
        Corridor subCorridor1 = subCorridors.stream()
                                            .filter(sc -> sc.getId() == 1)
                                            .findFirst()
                                            .get();
        Corridor subCorridor2 = subCorridors.stream()
                                            .filter(sc -> sc.getId() == 2)
                                            .findFirst()
                                            .get();
        assertNotNull(subCorridor1);
        assertNotNull(subCorridor2);
        if (HotelMotionSensorUtils.isNightTime()) {
            assertEquals(subCorridor1.getAirConditioner().getStatus(), Status.OFF);
            assertEquals(subCorridor2.getElectricLight().getStatus(), Status.ON);
        } else {
            assertEquals(subCorridor1.getAirConditioner().getStatus(), Status.ON);
            assertEquals(subCorridor2.getElectricLight().getStatus(), Status.OFF);
        }

        assertEquals(subCorridor2.getAirConditioner().getStatus(), Status.ON);
        assertTrue(HotelConsumptionPlan.getMaxPowerUnits().compareTo(HotelConsumptionPlan.getUsedPowerUnits(1)) == 1);

        controller.destroyManagerInstance();
    }

    @Test
    public void testNoMovement() throws AutomationControlException {
        int[] countParams = new int[] {2, 1, 2};
        int[] sensorMovementParams = new int[] {1, 2};

        controller.process(InputType.DEFAULT, new int[]{}, countParams, null);
        controller.process(InputType.MOVEMENT, sensorMovementParams, countParams, CorridorType.SUB_CORRIDOR);
        controller.process(InputType.NO_MOVEMENT, sensorMovementParams, countParams, CorridorType.SUB_CORRIDOR);

        List<Corridor> subCorridors = HotelMotionSensorImpl.getFloors()
                .get(1)
                .getSubCorridor();
        Corridor subCorridor1 = subCorridors.stream()
                .filter(sc -> sc.getId() == 1)
                .findFirst()
                .get();
        Corridor subCorridor2 = subCorridors.stream()
                .filter(sc -> sc.getId() == 2)
                .findFirst()
                .get();
        assertNotNull(subCorridor1);
        assertNotNull(subCorridor2);
        assertEquals(subCorridor1.getAirConditioner().getStatus(), Status.ON);
        assertEquals(subCorridor2.getElectricLight().getStatus(), Status.OFF);
        assertTrue(HotelConsumptionPlan.getMaxPowerUnits().compareTo(HotelConsumptionPlan.getUsedPowerUnits(1)) == 1);

        controller.destroyManagerInstance();
    }

    @Test(expected = AutomationControlException.class)
    public void testInvalidFloorCount() throws AutomationControlException {
        int[] countParams = new int[]{2, 1, 2};
        int[] sensorMovementParams = new int[]{5, 2};

        controller.process(InputType.DEFAULT, new int[]{}, countParams, null);
        controller.process(InputType.MOVEMENT, sensorMovementParams, countParams, CorridorType.SUB_CORRIDOR);

        controller.destroyManagerInstance();
    }

    @Test(expected = AutomationControlException.class)
    public void testInvalidSubCorridorCount() throws AutomationControlException {
        int[] countParams = new int[]{2, 1, 2};
        int[] sensorMovementParams = new int[]{1, 4};

        controller.process(InputType.DEFAULT, new int[]{}, countParams, null);
        controller.process(InputType.MOVEMENT, sensorMovementParams, countParams, CorridorType.SUB_CORRIDOR);

        controller.destroyManagerInstance();
    }

    @After
    public void cleanUp() {
        controller = null;
    }
}

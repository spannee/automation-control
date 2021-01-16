package com.sahaj.automationControl.plan;

import com.sahaj.automationControl.dto.Device;
import com.sahaj.automationControl.dto.Floor;
import com.sahaj.automationControl.enums.CorridorType;
import com.sahaj.automationControl.enums.DeviceType;
import com.sahaj.automationControl.enums.ErrorMessages;
import com.sahaj.automationControl.enums.Status;
import com.sahaj.automationControl.exception.AutomationControlException;
import com.sahaj.automationControl.utils.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

import static com.sahaj.automationControl.utils.HotelMotionSensorUtils.getTimeDifferenceInHours;
import static com.sahaj.automationControl.utils.HotelMotionSensorUtils.isNightTime;

public class HotelConsumptionPlan implements ConsumptionPlan {

    private static final Logger logger = LoggerFactory.getLogger("HotelConsumptionPlan");

    private static BigDecimal maxPowerUnits;

    private static Map<Integer, BigDecimal> usedPowerUnits;

    private static int floorCount;

    private static int mainCorridorCount;

    private static int subCorridorCount;

    private static Instant previousMovementTime;

    public HotelConsumptionPlan() {
        maxPowerUnits = new BigDecimal((mainCorridorCount * 15) + (subCorridorCount * 10));
    }

    /**
     * Consumption plan when there is movement in corridors
     * @param floors
     * @param floor
     * @param corridor
     * @param corridorType
     * @throws AutomationControlException
     */
    @Override
    public void movement(Map<Integer, Floor> floors, int floor, int corridor, CorridorType corridorType) throws AutomationControlException {
        ValidationUtils.validateSensorData(floor, corridor, corridorType);

        if (corridorType == CorridorType.SUB_CORRIDOR && isNightTime()) {
            //Switch on electric lights in the respective sub corridor
            switchDeviceStatus(floors, floor, corridor, Status.ON, DeviceType.ELECTRIC_LIGHT);

            //Switch off AirConditioners in other sub corridors
            switchDeviceStatus(floors, floor, corridor, Status.OFF, DeviceType.AIR_CONDITIONER);
        }
        setPreviousMovementTime(Instant.now());

        if (hasTotalPowerBeenConsumed(floor)) {
            logger.error(ErrorMessages.TOTAL_POWER_CONSUMED.getErrorMessages());
            throw new AutomationControlException(ErrorMessages.TOTAL_POWER_CONSUMED.getErrorMessages());
        }

    }

    /**
     * Consumption plan when there is no movement in corridors
     * @param floors
     * @param floor
     * @param corridor
     * @param corridorType
     * @throws AutomationControlException
     */
    @Override
    public void noMovement(Map<Integer, Floor> floors, int floor, int corridor, CorridorType corridorType) throws AutomationControlException {
        ValidationUtils.validateSensorData(floor, corridor, corridorType);

        if (corridorType == CorridorType.SUB_CORRIDOR && isNightTime()) {
            //Switch off electric lights in the respective sub corridor
            switchDeviceStatus(floors, floor, corridor, Status.OFF, DeviceType.ELECTRIC_LIGHT);

            //Switch on AirConditioners in other sub corridors
            switchDeviceStatus(floors, floor, corridor, Status.ON, DeviceType.AIR_CONDITIONER);
        }
        setPreviousMovementTime(Instant.now());

        if (hasTotalPowerBeenConsumed(floor)) {
            logger.error(ErrorMessages.TOTAL_POWER_CONSUMED.getErrorMessages());
            throw new AutomationControlException(ErrorMessages.TOTAL_POWER_CONSUMED.getErrorMessages());
        }
    }

    /**
     * Toggle devices
     * @param floors
     * @param floor
     * @param corridor
     * @param status
     * @param deviceType
     */
    @Override
    public void switchDeviceStatus(Map<Integer, Floor> floors, int floor, int corridor, Status status, DeviceType deviceType) {
        switch (deviceType) {
            case ELECTRIC_LIGHT:
                floors.get(floor)
                      .getSubCorridor()
                      .stream()
                      .filter(c -> c.getId() == corridor)
                      .findFirst()
                      .ifPresent(c -> {
                          Status currentStatus = c.getElectricLight().getStatus();
                          c.getElectricLight().setStatus(status);
                          powerConsumed(floor, c.getElectricLight(), currentStatus);
                      });
                break;
            case AIR_CONDITIONER:
                floors.get(floor)
                      .getSubCorridor()
                      .stream()
                      .filter(c -> c.getId() != corridor)
                      .forEach(c -> {
                          Status currentStatus = c.getAirConditioner().getStatus();
                          c.getAirConditioner().setStatus(status);
                          powerConsumed(floor, c.getAirConditioner(), currentStatus);
                      });
                break;
            default:
                break;
        }
    }

    /**
     * Calculate power consumed
     * @param floor
     * @param device
     * @param previousStatus
     * @throws AutomationControlException
     */
    @Override
    public void powerConsumed(int floor, Device device, Status previousStatus) {
        if (previousStatus == Status.OFF)
            return;

        BigDecimal timeDifferenceInHours = getTimeDifferenceInHours(previousMovementTime, Instant.now());
        BigDecimal unitsConsumed = timeDifferenceInHours.multiply(BigDecimal.valueOf(device.deviceType().getUnits()));
        usedPowerUnits.computeIfPresent(floor, (k, v) -> v.add(unitsConsumed));
    }

    /**
     * To check if power has exceeded the limit
     * @param floor
     * @return
     */
    @Override
    public boolean hasTotalPowerBeenConsumed(int floor) {
        if (usedPowerUnits.get(floor).compareTo(maxPowerUnits) == 1) {
            logger.info("Total Power Available for floor number " + floor + " - " + maxPowerUnits.toString());
            logger.info("Power Consumed So Far for floor number " + floor + " - " + usedPowerUnits.toString());
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public static void setPreviousMovementTime(Instant previousMovementTime) {
        HotelConsumptionPlan.previousMovementTime = previousMovementTime;
    }

    public static void setFloorCount(int floorCount) {
        HotelConsumptionPlan.floorCount = floorCount;
    }

    public static void setMainCorridorCount(int mainCorridorCount) {
        HotelConsumptionPlan.mainCorridorCount = mainCorridorCount;
    }

    public static void setSubCorridorCount(int subCorridorCount) {
        HotelConsumptionPlan.subCorridorCount = subCorridorCount;
    }

    public static void setUsedPowerUnits(Map<Integer, BigDecimal> usedPowerUnits) {
        HotelConsumptionPlan.usedPowerUnits = usedPowerUnits;
    }

    public static int getFloorCount() {
        return floorCount;
    }

    public static int getMainCorridorCount() {
        return mainCorridorCount;
    }

    public static int getSubCorridorCount() {
        return subCorridorCount;
    }

    public static BigDecimal getMaxPowerUnits() {
        return maxPowerUnits;
    }

    public static BigDecimal getUsedPowerUnits(int floor) {
        return usedPowerUnits.get(floor);
    }


}

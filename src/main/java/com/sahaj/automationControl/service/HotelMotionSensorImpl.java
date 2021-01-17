package com.sahaj.automationControl.service;

import com.sahaj.automationControl.dto.*;
import com.sahaj.automationControl.enums.CorridorType;
import com.sahaj.automationControl.exception.AutomationControlException;
import com.sahaj.automationControl.plan.ConsumptionPlan;
import com.sahaj.automationControl.plan.HotelConsumptionPlan;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import static com.sahaj.automationControl.utils.HotelMotionSensorUtils.printResultString;


public class HotelMotionSensorImpl implements MotionSensor {

    private static final int DEFAULT_DEVICE_ID = 1;

    private static MotionSensor instance;

    private static Map<Integer, Floor> floors;

    private static int floorCount;

    private static int mainCorridorCount;

    private static int subCorridorCount;

    private static BigDecimal maxPowerUnits;

    private static Instant previousMovementTime;

    private HotelMotionSensorImpl(int floorCount, int mainCorridorCount, int subCorridorCount) {
        this.floorCount = floorCount;
        this.mainCorridorCount = mainCorridorCount;
        this.subCorridorCount = subCorridorCount;
        this.maxPowerUnits = HotelConsumptionPlan.getMaxPowerUnits(mainCorridorCount, subCorridorCount);
        this.previousMovementTime = Instant.now();

        initializeHotel(floorCount, mainCorridorCount, subCorridorCount);

        Map<Integer, BigDecimal> usedPowerUnits = new ConcurrentHashMap<>();
        IntStream.range(1, floorCount+1)
                 .forEach(i -> usedPowerUnits.put(i, BigDecimal.ZERO));
        HotelConsumptionPlan.setUsedPowerUnits(usedPowerUnits);
    }

    /**
     * Default state of electrical devices
     * @param floorCount
     * @param mainCorridorCount
     * @param subCorridorCount
     */
    private void initializeHotel(int floorCount, int mainCorridorCount, int subCorridorCount) {
        Map<Integer, Floor> tempFloors = new HashMap<>();
        IntStream.range(1, floorCount+1)
                 .forEach(fc -> {
                     List<Corridor> mcs = initializeMainCorridors(mainCorridorCount);
                     List<Corridor> scs = initializeSubCorridors(subCorridorCount);
                     Floor floor = new Floor(fc, mcs, scs);
                     tempFloors.put(fc, floor);
                 });
        floors = Collections.unmodifiableMap(tempFloors);
        printResultString(floors);
    }

    /**
     * Default state of electrical devices in main corridors
     * @param corridorCount
     * @return
     */
    private List<Corridor> initializeMainCorridors(int corridorCount) {
        List<Corridor> mainCorridors = new ArrayList<>();
        IntStream.range(1, corridorCount+1)
                 .forEach(mc -> {
                     Device electricLight = new ElectricLight(DEFAULT_DEVICE_ID);
                     Device airConditioner = new AirConditioner(DEFAULT_DEVICE_ID);
                     Corridor mainCorridor = new MainCorridor(mc, electricLight, airConditioner);
                     mainCorridors.add(mainCorridor);
                });
        return mainCorridors;
    }

    /**
     * Default state of electrical devices in sub corridors
     * @param corridorCount
     * @return
     */
    private List<Corridor> initializeSubCorridors(int corridorCount) {
        List<Corridor> mainCorridors = new ArrayList<>();
        IntStream.range(1, corridorCount+1)
                 .forEach(mc -> {
                     Device electricLight = new ElectricLight(DEFAULT_DEVICE_ID);
                     Device airConditioner = new AirConditioner(DEFAULT_DEVICE_ID);
                     Corridor mainCorridor = new SubCorridor(mc, electricLight, airConditioner);
                     mainCorridors.add(mainCorridor);
                 });
        return mainCorridors;
    }

    /**
     * Controls devices and calculates power when there is movement in the corridor
     * based on the consumption plan
     * @param floor
     * @param corridor
     * @param corridorType
     * @param consumptionPlan
     * @throws AutomationControlException
     */
    @Override
    public void movement(int floor, int corridor, CorridorType corridorType, ConsumptionPlan consumptionPlan) throws AutomationControlException {
        consumptionPlan.movement(floors, floor, corridor, corridorType);
        printResultString(floors);
        setPreviousMovementTime(Instant.now());
    }

    /**
     * Controls devices and calculates power when there is no movement in the corridor
     * for more than a minute based on the consumption plan
     * @param floor
     * @param corridor
     * @param corridorType
     * @param consumptionPlan
     * @throws AutomationControlException
     */
    @Override
    public void noMovement(int floor, int corridor, CorridorType corridorType, ConsumptionPlan consumptionPlan) throws AutomationControlException {
        consumptionPlan.noMovement(floors, floor, corridor, corridorType);
        printResultString(floors);
        setPreviousMovementTime(Instant.now());
    }

    /**
     * Singleton
     * @param floorCount
     * @param mainCorridorCount
     * @param subCorridorCount
     * @return
     */
    public static MotionSensor getInstance(int floorCount, int mainCorridorCount, int subCorridorCount) {
        if (instance == null) {
            synchronized (HotelMotionSensorImpl.class) {
                if (instance == null) {
                    instance = new HotelMotionSensorImpl(floorCount, mainCorridorCount, subCorridorCount);
                }
            }
        }
        return instance;
    }

    /**
     * Destroys the instance at the end
     */
    @Override
    public void destroy() {
        instance = null;
    }

    public static Map<Integer, Floor> getFloors() {
        return floors;
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

    public static Instant getPreviousMovementTime() {
        return previousMovementTime;
    }

    public static void setPreviousMovementTime(Instant previousMovementTime) {
        HotelMotionSensorImpl.previousMovementTime = previousMovementTime;
    }

}

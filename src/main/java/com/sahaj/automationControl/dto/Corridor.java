package com.sahaj.automationControl.dto;

import com.sahaj.automationControl.enums.DeviceType;
import com.sahaj.automationControl.enums.Status;

import java.util.Objects;

/**
 * Represents different types of corridors
 */
public abstract class Corridor {

    private int id;

    private Device electricLight;

    private Device airConditioner;

    public Corridor(int id, Device electricLight, Device airConditioner) {
        this.id = id;
        this.electricLight = electricLight;
        this.airConditioner = airConditioner;
        this.electricLight.setStatus(getDefaultState(DeviceType.ELECTRIC_LIGHT));
        this.airConditioner.setStatus(getDefaultState(DeviceType.AIR_CONDITIONER));
    }

    abstract Status getDefaultState(DeviceType deviceType);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Device getElectricLight() {
        return electricLight;
    }

    public void setElectricLight(Device electricLight) {
        this.electricLight = electricLight;
    }

    public Device getAirConditioner() {
        return airConditioner;
    }

    public void setAirConditioner(Device airConditioner) {
        this.airConditioner = airConditioner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Corridor)) return false;
        Corridor corridor = (Corridor) o;
        return getId() == corridor.getId() &&
                Objects.equals(getElectricLight(), corridor.getElectricLight()) &&
                Objects.equals(getAirConditioner(), corridor.getAirConditioner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getElectricLight(), getAirConditioner());
    }

    @Override
    public String toString() {
        String type = "MainCorridor";
        if (this instanceof SubCorridor)
            type = "SubCorridor";
        return type + " " + id + " " + electricLight + " " + airConditioner + " ";
    }

}

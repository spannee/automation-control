package com.sahaj.automationControl.dto;

import com.sahaj.automationControl.enums.DeviceType;
import com.sahaj.automationControl.enums.Status;

import java.util.Objects;

/**
 * Represents all electrical devices
 */
public abstract class Device {

    private int id;

    private Status status;

    public Device(int id) {
        this.id = id;
    }

    public abstract DeviceType deviceType();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        if (status == null)
            return Status.OFF;
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Device)) return false;
        Device device = (Device) o;
        return getId() == device.getId() &&
                getStatus() == device.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getStatus());
    }


    @Override
    public String toString() {
        String type = "Light";
        if (this instanceof AirConditioner)
            type = "AirConditioner";
        return type + " " + id + ": " + status;
    }

}

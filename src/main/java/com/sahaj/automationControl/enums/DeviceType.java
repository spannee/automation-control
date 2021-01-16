package com.sahaj.automationControl.enums;

public enum DeviceType {

    ELECTRIC_LIGHT(5),
    AIR_CONDITIONER(10);

    private int units;

    DeviceType(int units) {
        this.units = units;
    }

    public int getUnits() {
        return units;
    }

}

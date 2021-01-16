package com.sahaj.automationControl.enums;

/**
 * An enum containing all error message constants so that they can be reused.
 */
public enum ErrorMessages {

    AUTOMATION_CONTROL_NOT_EXISTS("An automation control system does not exist to get a response"),
    INVALID_FLOOR_NUMBER("The given floor number exceeds the total number of floors in the hotel"),
    INVALID_MC_ID("The given main corridor number exceeds the total number of main corridors in the floor"),
    INVALID_SC_ID("The given sub corridor number exceeds the total number of sub corridors in the floor"),
    TOTAL_POWER_CONSUMED("Warning! The total power available for this floor has been consumed"),
    INVALID_INPUT("The provided input is invalid");

    private String errorMessages;

    public String getErrorMessages() {
        return errorMessages;
    }

    ErrorMessages(String errorMessages) {
        this.errorMessages = errorMessages;
    }
}

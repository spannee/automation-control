package com.sahaj.automationControl.enums;

public enum Status {

    ON( "on"),
    OFF("off");

    private String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}

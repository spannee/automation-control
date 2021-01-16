package com.sahaj.automationControl.exception;

public class AutomationControlException extends Exception {

    /**
     *
     * @param message
     */
    public AutomationControlException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param throwable
     */
    public AutomationControlException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

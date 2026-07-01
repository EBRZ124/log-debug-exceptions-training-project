package com.bootcamp.smarthome.exception;

public class DeviceOfflineException extends HomeAutomationException {

    public DeviceOfflineException(String message) {
        super(message);
    }

    public DeviceOfflineException(String message, Throwable cause) {
        super(message, cause);
    }
}
package com.everest.courier.Exceptions;

public class ConfigurationException extends GeneralException {
    public ConfigurationException(String msg) {
        super(msg);
    }

    public ConfigurationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

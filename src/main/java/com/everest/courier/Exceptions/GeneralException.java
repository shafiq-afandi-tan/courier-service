package com.everest.courier.Exceptions;

public class GeneralException extends Exception {
    public GeneralException(String msg) {
        super(msg);
    }

    public GeneralException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

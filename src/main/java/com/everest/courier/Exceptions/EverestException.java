package com.everest.courier.Exceptions;

public class EverestException extends Exception {
    public EverestException(String msg) {
        super(msg);
    }

    public EverestException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

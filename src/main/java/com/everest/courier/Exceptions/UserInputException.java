package com.everest.courier.Exceptions;

public class UserInputException extends EverestException {
    public UserInputException(String message) {
        super(message);
    }

    public UserInputException(String message, Throwable cause) {
        super(message, cause);
    }
}

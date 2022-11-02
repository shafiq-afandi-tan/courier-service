package com.everest.courier.Exceptions;

public class UserInputFieldMissingPackageId extends UserInputException {
    public UserInputFieldMissingPackageId() {
        super("Missing field - package_id");
    }
}

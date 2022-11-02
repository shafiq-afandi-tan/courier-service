package com.everest.courier.Exceptions;

public class UserInputFieldMissingPackageInfo extends UserInputException {
    public UserInputFieldMissingPackageInfo() {
        super("Missing field - package_info");
    }
}

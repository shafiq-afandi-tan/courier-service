package com.everest.courier.Exceptions;

public class UserInputFieldMissingVehicleSpeed extends UserInputException {
    public UserInputFieldMissingVehicleSpeed() {
        super("Missing field - vehicle_speed");
    }
}

package com.everest.courier.Exceptions;

public class UserInputFieldMissingVehicleCapacity extends UserInputException {
    public UserInputFieldMissingVehicleCapacity() {
        super("Missing field - vehicle_capacity");
    }
}

package com.everest.courier.Exceptions;

public class UserInputFieldMissingBaseDeliveryCost extends UserInputException {

    public UserInputFieldMissingBaseDeliveryCost() {
        super("Missing field - base_delivery_cost");
    }
}

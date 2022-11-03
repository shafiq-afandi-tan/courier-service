package com.everest.courier;

import java.math.BigDecimal;

public class ShippingVehicle {
    public String vehicleNo;
    public int speed;
    public BigDecimal availableAfter;
    public int capacity;

    public ShippingVehicle() {}

    public ShippingVehicle(String vehicleNo, int speed, BigDecimal availableAfter, int capacity) {
        this.vehicleNo = vehicleNo;
        this.speed = speed;
        this.availableAfter = availableAfter;
        this.capacity = capacity;
    }
}

package com.everest.courier;

import java.util.ArrayList;
import java.util.List;

public class TimeContext extends CostContext {
    public int noOfVehicle;

    public int vehicleSpeed;

    public int vehicleCapacity;

    public List<ShippingVehicle> vehicles = new ArrayList<>();

    public TimeContext() {
        this.type = ServiceType.TIME;
    }
}

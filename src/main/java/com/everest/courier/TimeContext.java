package com.everest.courier;

import java.util.ArrayList;
import java.util.List;

public class TimeContext extends Context {
    public int noOfVehicle;

    public int vehicleSpeed;

    public int vehicleCapacity;

    public List<ShippingVehicle> vehicles = new ArrayList<>();
}

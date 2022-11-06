package com.everest.courier;

import java.io.DataInput;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class TimeEstimator {
    public int vehicleSpeed;

    public TimeEstimator(int vehicleSpeed) {
        this.vehicleSpeed = vehicleSpeed;
    }
    public BigDecimal calculateDeliveryTime(ShipmentItem item) {
        return BigDecimal.valueOf(item.distance).divide(BigDecimal.valueOf(vehicleSpeed), 2, RoundingMode.FLOOR);
    }

    public BigDecimal calculateArrivalItem(ShippingVehicle vehicle, ShipmentItem item) {
        return vehicle.availableAfter.add(item.deliveryTime);
    }

    public BigDecimal calculateAvailableTime(ShippingVehicle vehicle, Shipment shipment) {
        return vehicle.availableAfter.add(shipment.getDeliveryItem().multiply(BigDecimal.valueOf(2)));
    }
}

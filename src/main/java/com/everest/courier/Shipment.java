package com.everest.courier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Shipment {
    public String vehicleNo;
    public List<ShipmentItem> items = new ArrayList<>();

    public int getWeight() {
        int weight = 0;
        for(ShipmentItem item: items) {
            weight += item.weight;
        }
        return weight;
    }

    public BigDecimal getDeliveryItem() {
        BigDecimal value = BigDecimal.valueOf(0);
        for(ShipmentItem item: items) {
            if(item.deliveryTime.compareTo(value) > 0)
                value = item.deliveryTime;
        }
        return value;
    }
}

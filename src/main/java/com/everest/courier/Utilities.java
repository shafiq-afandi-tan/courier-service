package com.everest.courier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utilities {
    public Shipment findNextShipmentOf(List<ShipmentItem> items, int weightLimit) {
        Shipment selected = null;
        int counter = 0;
        int weight = 0;
        while(++counter < Math.pow(2, items.size())) {
            Shipment shipment = new Shipment();
            for(int i = 0; i < items.size(); i++) {
                int mask = 1 << i;
                if((counter & mask) == mask) {
                    ShipmentItem item = items.get(i);
                    weight += item.weight;
                    shipment.items.add(item);
                }
            }
            if(shipment.items.size() > 0 && shipment.getWeight() <= weightLimit
                    && (selected == null || shipment.items.size() > selected.items.size() ||
                    shipment.items.size() == selected.items.size() && shipment.getWeight() > selected.getWeight())) {
                selected = shipment;
            }
        }
        return selected;
    }

    public void clearShipmentItems(List<ShipmentItem> items, List<ShipmentItem> pool) {
        for(ShipmentItem item: items)
            pool.remove(item);
    }

    public ShippingVehicle findVehicle(List<ShippingVehicle> vehicles) {
        ShippingVehicle selected = null;
        for (ShippingVehicle vehicle: vehicles) {
            int value = vehicle.availableAfter.compareTo(BigDecimal.valueOf(0));
            if (value == 0) {
                selected = vehicle;
                break;
            } else if (selected == null) {
                selected = vehicle;
                continue;
            }
            if (vehicle.availableAfter.compareTo(selected.availableAfter) < 0)
                selected = vehicle;
        }
        return selected;
    }
}

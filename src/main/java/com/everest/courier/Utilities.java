package com.everest.courier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utilities {
    List<Shipment> findShipmentsOf(ShipmentItem item, List<ShipmentItem> items, int weightLimit) {
        List<Shipment> shipments = new ArrayList<>();
        if(item.weight > weightLimit)
            return shipments;
        List<ShipmentItem> clones = new ArrayList<>(items);
        do {
            Shipment shipment = new Shipment();
            shipment.items.add(item);
            int weight = item.weight;
            int size = clones.size(), count = 0;
            for (ShipmentItem clone : clones) {
                weight += clone.weight;
                if (weight <= weightLimit) {
                    shipment.items.add(clone);
                    count++;
                }
                else {
                    clones.remove(0);
                    break;
                }
            }
            shipments.add(shipment);
            if(count == size)
                break;
        } while(clones.size() > 0);
        return shipments;
    }

    public Shipment findHeaviestShipment(List<Shipment> shipments) {
        Shipment selected = null;
        for(Shipment shipment: shipments) {
            if(selected == null)
                selected = shipment;
            else {
                if(shipment.getWeight() > selected.getWeight())
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

    public void sortShipments(List<Shipment> shipments) {
        Collections.sort(shipments, (left, right)->{
            return right.getWeight() - left.getWeight();
        });
    }
}

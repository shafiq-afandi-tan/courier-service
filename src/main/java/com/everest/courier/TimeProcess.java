package com.everest.courier;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TimeProcess extends CostProcess {
    Factory factory;
    Utilities utilities;
    public TimeProcess(Factory factory) {
        super(factory);
        this.factory = factory;
        this.utilities = factory.getUtilities();
    }

    @Override
    public void run(Context context) {
        super.run(context);
        List<ShipmentItem> itemPool = new ArrayList<>(context.shipmentItems);
        List<Shipment> shipments = new ArrayList<>();
        while(itemPool.size() > 0) {
            Shipment shipment = utilities.findNextShipmentOf(itemPool, context.vehicleCapacity);
            if(shipment == null)
                break;
            else {
                shipments.add(shipment);
                utilities.clearShipmentItems(shipment.items, itemPool);
            }
        }
        for(Shipment shipment: shipments) {
            ShippingVehicle vehicle = utilities.findVehicle(context.vehicles);
            for (ShipmentItem item : shipment.items)
                item.deliveryTime = BigDecimal.valueOf(item.distance).divide(BigDecimal.valueOf(context.vehicleSpeed), 2, RoundingMode.FLOOR);

            for (ShipmentItem shipmentItem : shipment.items) {
                ResultItem item = context.resultMap.get(shipmentItem.packageId);
                item.arrivalTime = vehicle.availableAfter.add(shipmentItem.deliveryTime);
            }
            BigDecimal time = vehicle.availableAfter.add(shipment.getDeliveryItem().multiply(BigDecimal.valueOf(2)));
            if (vehicle != null) {
                vehicle.availableAfter = time;
                shipment.vehicleNo = vehicle.vehicleNo;
            }
        }
    }
}


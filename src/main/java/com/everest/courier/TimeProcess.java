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
        List<Shipment> finalShipments = new ArrayList<>();

        while(itemPool.size() > 0) {
            List<ShipmentItem> clonePool = new ArrayList<>(itemPool);
            List<Shipment> shipments = new ArrayList<>();

            while (clonePool.size() > 0) {
                ShipmentItem item = clonePool.remove(0);
                List<Shipment> _shipments = utilities.findShipmentsOf(item, clonePool, context.vehicleCapacity);
                shipments.addAll(_shipments);
            }

            Shipment shipment = utilities.findHeaviestShipment(shipments);

            if (shipment != null) {
                finalShipments.add(shipment);
                utilities.clearShipmentItems(shipment.items, itemPool);
            }
            else
                break;
        }

        utilities.sortShipments(finalShipments);

        for(Shipment _shipment: finalShipments) {
            ShippingVehicle vehicle = utilities.findVehicle(context.vehicles);
            for (ShipmentItem item : _shipment.items)
                item.deliveryTime = BigDecimal.valueOf(item.distance).divide(BigDecimal.valueOf(context.vehicleSpeed), 2, RoundingMode.FLOOR);

            for (ShipmentItem shipmentItem : _shipment.items) {
                ResultItem item = context.resultMap.get(shipmentItem.packageId);
                item.arrivalTime = vehicle.availableAfter.add(shipmentItem.deliveryTime);
            }

            BigDecimal time = vehicle.availableAfter.add(_shipment.getDeliveryItem().multiply(BigDecimal.valueOf(2)));

            if (vehicle != null) {
                vehicle.availableAfter = time;
                _shipment.vehicleNo = vehicle.vehicleNo;
            }
        }
    }
}

package com.everest.courier;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class TestTimeProcess {
    @Test
    public void testTimeProcess_exceedMaxLoadCapacity() {
        Context context = new Context();
        context.type = ServiceType.TIME;
        context.weightFactor = 10;
        context.distanceFactor = 5;
        context.baseDeliveryCost = new BigDecimal(100);
        context.noOfPackages = 3;
        context.shipmentItems.add(new ShipmentItem("PKG1", 205, 50, null));
        context.shipmentItems.add(new ShipmentItem("PKG2", 210, 50, null));
        context.shipmentItems.add(new ShipmentItem("PKG3", 215, 50, null));
        context.noOfVehicle = 1;
        context.vehicleSpeed = 70;
        context.vehicleCapacity = 200;
        context.vehicles.add(new ShippingVehicle(String.format("%02d", 1), context.vehicleSpeed, BigDecimal.valueOf(0), context.vehicleCapacity));
        new Factory().getProcess(ServiceType.TIME).run(context);
        assertEquals(3, context.resultItems.size());
        for(ResultItem item: context.resultItems) {
            assertNull(item.arrivalTime);
        }
    }

    @Test
    public void testTimeProcess_heavierPackageFirst() {
        Context context = new Context();
        context.type = ServiceType.TIME;
        context.weightFactor = 10;
        context.distanceFactor = 5;
        context.baseDeliveryCost = new BigDecimal(100);
        context.noOfPackages = 3;
        context.shipmentItems.add(new ShipmentItem("PKG1", 100, 50, null));
        context.shipmentItems.add(new ShipmentItem("PKG2", 150, 62, null));
        context.shipmentItems.add(new ShipmentItem("PKG3", 200, 75, null));
        context.noOfVehicle = 1;
        context.vehicleSpeed = 70;
        context.vehicleCapacity = 200;
        context.vehicles.add(new ShippingVehicle(String.format("%02d", 1), context.vehicleSpeed, BigDecimal.valueOf(0), context.vehicleCapacity));
        new Factory().getProcess(ServiceType.TIME).run(context);
        assertEquals(3, context.resultItems.size());
        assertTrue(BigDecimal.valueOf(4.61).compareTo(context.resultItems.get(0).arrivalTime) == 0);
        assertTrue(BigDecimal.valueOf(3.02).compareTo(context.resultItems.get(1).arrivalTime) == 0);
        assertTrue(BigDecimal.valueOf(1.07).compareTo(context.resultItems.get(2).arrivalTime) == 0);
    }

    @Test
    public void testTimeProcess_maximizeLoadPerShipment() {
        Context context = new Context();
        context.type = ServiceType.TIME;
        context.weightFactor = 10;
        context.distanceFactor = 5;
        context.baseDeliveryCost = new BigDecimal(100);
        context.noOfPackages = 3;
        context.shipmentItems.add(new ShipmentItem("PKG1", 60, 50, null));
        context.shipmentItems.add(new ShipmentItem("PKG2", 65, 62, null));
        context.shipmentItems.add(new ShipmentItem("PKG3", 70, 75, null));
        context.noOfVehicle = 1;
        context.vehicleSpeed = 70;
        context.vehicleCapacity = 200;
        context.vehicles.add(new ShippingVehicle(String.format("%02d", 1), context.vehicleSpeed, BigDecimal.valueOf(0), context.vehicleCapacity));
        new Factory().getProcess(ServiceType.TIME).run(context);
        assertEquals(3, context.resultItems.size());
        assertTrue(BigDecimal.valueOf(0.71).compareTo(context.resultItems.get(0).arrivalTime) == 0);
        assertTrue(BigDecimal.valueOf(0.88).compareTo(context.resultItems.get(1).arrivalTime) == 0);
        assertTrue(BigDecimal.valueOf(1.07).compareTo(context.resultItems.get(2).arrivalTime) == 0);
    }

    @Test
    public void testTimeProcess_heavierGroupingFirst() {
        Context context = new Context();
        context.type = ServiceType.TIME;
        context.weightFactor = 10;
        context.distanceFactor = 5;
        context.baseDeliveryCost = new BigDecimal(100);
        context.noOfPackages = 4;
        context.shipmentItems.add(new ShipmentItem("PKG1", 75, 50, null));
        context.shipmentItems.add(new ShipmentItem("PKG2", 85, 62, null));
        context.shipmentItems.add(new ShipmentItem("PKG3", 100, 75, null));
        context.shipmentItems.add(new ShipmentItem("PKG4", 110, 80, null));
        context.noOfVehicle = 1;
        context.vehicleSpeed = 70;
        context.vehicleCapacity = 200;
        context.vehicles.add(new ShippingVehicle(String.format("%02d", 1), context.vehicleSpeed, BigDecimal.valueOf(0), context.vehicleCapacity));
        new Factory().getProcess(ServiceType.TIME).run(context);
        assertEquals(4, context.resultItems.size());
        assertTrue(BigDecimal.valueOf(2.99).compareTo(context.resultItems.get(0).arrivalTime) == 0);
        assertTrue(BigDecimal.valueOf(0.88).compareTo(context.resultItems.get(1).arrivalTime) == 0);
        assertTrue(BigDecimal.valueOf(3.35).compareTo(context.resultItems.get(2).arrivalTime) == 0);
        assertTrue(BigDecimal.valueOf(1.14).compareTo(context.resultItems.get(3).arrivalTime) == 0);
    }

    @Test
    public void testTimeProcess_heavierPackageFirstWithMultipleVehicles() {
        Context context = new Context();
        context.type = ServiceType.TIME;
        context.weightFactor = 10;
        context.distanceFactor = 5;
        context.baseDeliveryCost = new BigDecimal(100);
        context.noOfPackages = 4;
        context.shipmentItems.add(new ShipmentItem("PKG1", 100, 50, null));
        context.shipmentItems.add(new ShipmentItem("PKG2", 125, 62, null));
        context.shipmentItems.add(new ShipmentItem("PKG3", 150, 75, null));
        context.shipmentItems.add(new ShipmentItem("PKG4", 175, 80, null));
        context.noOfVehicle = 2;
        context.vehicleSpeed = 70;
        context.vehicleCapacity = 200;
        context.vehicles.add(new ShippingVehicle(String.format("%02d", 1), context.vehicleSpeed, BigDecimal.valueOf(0), context.vehicleCapacity));
        context.vehicles.add(new ShippingVehicle(String.format("%02d", 2), context.vehicleSpeed, BigDecimal.valueOf(0), context.vehicleCapacity));
        new Factory().getProcess(ServiceType.TIME).run(context);
        assertEquals(4, context.resultItems.size());
        assertTrue(BigDecimal.valueOf(2.99).compareTo(context.resultItems.get(0).arrivalTime) == 0);
        assertTrue(BigDecimal.valueOf(3.02).compareTo(context.resultItems.get(1).arrivalTime) == 0);
        assertTrue(BigDecimal.valueOf(1.07).compareTo(context.resultItems.get(2).arrivalTime) == 0);
        assertTrue(BigDecimal.valueOf(1.14).compareTo(context.resultItems.get(3).arrivalTime) == 0);
    }
}

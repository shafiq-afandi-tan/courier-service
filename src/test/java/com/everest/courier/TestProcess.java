package com.everest.courier;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class TestProcess {
    private void testCostProcess_withoutDiscount(String strOfferCode) {
        CostContext context = new CostContext();
        context.weightFactor = 10;
        context.distanceFactor = 5;
        context.baseDeliveryCost = new BigDecimal(100);
        context.noOfPackages = 1;
        context.shipmentItems.add(new ShipmentItem("PKG1", 50, 30, strOfferCode));
        context.offerCodes.add(new OfferCode("OFR001", null, 200, null, 50, 10));
        context.offerCodes.add(new OfferCode("OFR002", 150, 200, 50, 80, 5));
        new Factory().getProcess(ServiceType.COST).run(context);
        assertEquals(1, context.resultItems.size());
        CostItem costItem = (CostItem) context.resultItems.get(0);
        assertTrue(BigDecimal.valueOf(0).compareTo(costItem.discount) == 0);
        assertTrue(BigDecimal.valueOf(750).compareTo(costItem.totalCost) == 0);
    }

    @Test
    public void testCostProcess_withoutOfferCode() {
        testCostProcess_withoutDiscount(null);
    }

    @Test
    public void testCostProcess_offerCodeNotFound() {
        testCostProcess_withoutDiscount("OFR999");
    }

    @Test
    public void testCostProcess_outOfRange() {
        CostContext context = new CostContext();
        context.weightFactor = 10;
        context.distanceFactor = 5;
        context.baseDeliveryCost = new BigDecimal(100);
        context.noOfPackages = 6;
        context.shipmentItems.add(new ShipmentItem("PKG1", 300, 30, "OFR001"));
        context.shipmentItems.add(new ShipmentItem("PKG2", 50, 55, "OFR001"));
        context.shipmentItems.add(new ShipmentItem("PKG3", 50, 55, "OFR002"));
        context.shipmentItems.add(new ShipmentItem("PKG4", 205, 55, "OFR002"));
        context.shipmentItems.add(new ShipmentItem("PKG5", 160, 30, "OFR002"));
        context.shipmentItems.add(new ShipmentItem("PKG6", 160, 85, "OFR002"));
        context.offerCodes.add(new OfferCode("OFR001", null, 200, null, 50, 10));
        context.offerCodes.add(new OfferCode("OFR002", 150, 200, 50, 80, 5));
        new Factory().getProcess(ServiceType.COST).run(context);
        assertEquals(6, context.resultItems.size());
        CostItem costItem = (CostItem)context.resultItems.get(0);
        assertTrue(BigDecimal.valueOf(0).compareTo(costItem.discount) == 0);
        assertTrue(BigDecimal.valueOf(3250).compareTo(costItem.totalCost) == 0);
        costItem = (CostItem)context.resultItems.get(1);
        assertTrue(BigDecimal.valueOf(0).compareTo(costItem.discount) == 0);
        assertTrue(BigDecimal.valueOf(875).compareTo(costItem.totalCost) == 0);
        costItem = (CostItem)context.resultItems.get(2);
        assertTrue(BigDecimal.valueOf(0).compareTo(costItem.discount) == 0);
        assertTrue(BigDecimal.valueOf(875).compareTo(costItem.totalCost) == 0);
        costItem = (CostItem)context.resultItems.get(3);
        assertTrue(BigDecimal.valueOf(0).compareTo(costItem.discount) == 0);
        assertTrue(BigDecimal.valueOf(2425).compareTo(costItem.totalCost) == 0);
        costItem = (CostItem)context.resultItems.get(4);
        assertTrue(BigDecimal.valueOf(0).compareTo(costItem.discount) == 0);
        assertTrue(BigDecimal.valueOf(1850).compareTo(costItem.totalCost) == 0);
        costItem = (CostItem)context.resultItems.get(5);
        assertTrue(BigDecimal.valueOf(0).compareTo(costItem.discount) == 0);
        assertTrue(BigDecimal.valueOf(2125).compareTo(costItem.totalCost) == 0);
    }

    @Test
    public void testCostProcess_offerCodeOFR001() {
        CostContext context = new CostContext();
        context.weightFactor = 10;
        context.distanceFactor = 5;
        context.baseDeliveryCost = new BigDecimal(100);
        context.noOfPackages = 1;
        context.shipmentItems.add(new ShipmentItem("PKG1", 100, 45, "OFR001"));
        context.offerCodes.add(new OfferCode("OFR001", null, 200, null, 50, 10));
        context.offerCodes.add(new OfferCode("OFR002", 150, 200, 50, 80, 5));
        new Factory().getProcess(ServiceType.COST).run(context);
        assertEquals(1, context.resultItems.size());
        CostItem costItem = (CostItem)context.resultItems.get(0);
        assertTrue(BigDecimal.valueOf(132.50).compareTo(costItem.discount) == 0);
        assertTrue(BigDecimal.valueOf(1192.50).compareTo(costItem.totalCost) == 0);

    }

    @Test
    public void testCostProcess_offerCodeOFR002() {
        CostContext context = new CostContext();
        context.weightFactor = 10;
        context.distanceFactor = 5;
        context.baseDeliveryCost = new BigDecimal(100);
        context.noOfPackages = 1;
        context.shipmentItems.add(new ShipmentItem("PKG1", 155, 55, "OFR002"));
        context.offerCodes.add(new OfferCode("OFR001", null, 200, null, 50, 10));
        context.offerCodes.add(new OfferCode("OFR002", 150, 200, 50, 80, 5));
        new Factory().getProcess(ServiceType.COST).run(context);
        assertEquals(1, context.resultItems.size());
        CostItem costItem = (CostItem)context.resultItems.get(0);
        assertTrue(BigDecimal.valueOf(96.25).compareTo(costItem.discount) == 0);
        assertTrue(BigDecimal.valueOf(1828.75).compareTo(costItem.totalCost) == 0);
    }

    @Test
    public void testTimeProcess_exceedMaxLoadCapacity() {
        TimeContext context = new TimeContext();
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
            TimeItem timeItem = (TimeItem)item;
            assertNull(timeItem.arrivalTime);
        }
    }

    @Test
    public void testTimeProcess_heavierPackageFirst() {
        TimeContext context = new TimeContext();
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
        assertTrue(BigDecimal.valueOf(4.61).compareTo(((TimeItem)context.resultItems.get(0)).arrivalTime) == 0);
        assertTrue(BigDecimal.valueOf(3.02).compareTo(((TimeItem)context.resultItems.get(1)).arrivalTime) == 0);
        assertTrue(BigDecimal.valueOf(1.07).compareTo(((TimeItem)context.resultItems.get(2)).arrivalTime) == 0);
    }

    @Test
    public void testTimeProcess_maximizeLoadPerShipment() {
        TimeContext context = new TimeContext();
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
        assertTrue(BigDecimal.valueOf(0.71).compareTo(((TimeItem)context.resultItems.get(0)).arrivalTime) == 0);
        assertTrue(BigDecimal.valueOf(0.88).compareTo(((TimeItem)context.resultItems.get(1)).arrivalTime) == 0);
        assertTrue(BigDecimal.valueOf(1.07).compareTo(((TimeItem)context.resultItems.get(2)).arrivalTime) == 0);
    }

    @Test
    public void testTimeProcess_heavierGroupingFirst() {
        TimeContext context = new TimeContext();
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
        assertTrue(BigDecimal.valueOf(2.99).compareTo(((TimeItem)context.resultItems.get(0)).arrivalTime) == 0);
        assertTrue(BigDecimal.valueOf(0.88).compareTo(((TimeItem)context.resultItems.get(1)).arrivalTime) == 0);
        assertTrue(BigDecimal.valueOf(3.35).compareTo(((TimeItem)context.resultItems.get(2)).arrivalTime) == 0);
        assertTrue(BigDecimal.valueOf(1.14).compareTo(((TimeItem)context.resultItems.get(3)).arrivalTime) == 0);
    }

    @Test
    public void testTimeProcess_heavierPackageFirstWithMultipleVehicles() {
        TimeContext context = new TimeContext();
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
        assertTrue(BigDecimal.valueOf(2.99).compareTo(((TimeItem)context.resultItems.get(0)).arrivalTime) == 0);
        assertTrue(BigDecimal.valueOf(3.02).compareTo(((TimeItem)context.resultItems.get(1)).arrivalTime) == 0);
        assertTrue(BigDecimal.valueOf(1.07).compareTo(((TimeItem)context.resultItems.get(2)).arrivalTime) == 0);
        assertTrue(BigDecimal.valueOf(1.14).compareTo(((TimeItem)context.resultItems.get(3)).arrivalTime) == 0);
    }
}

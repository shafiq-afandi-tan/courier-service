package com.everest.courier;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestProcess {
    private void testRun_withoutDiscount(String strOfferCode) {
        CostContext context = new CostContext();
        context.weightFactor = 10;
        context.distanceFactor = 5;
        context.baseDeliveryCost = new BigDecimal(100);
        context.noOfPackages = 1;
        context.shipmentItems.add(new ShipmentItem("PKG1", 50, 30, strOfferCode));
        context.offerCodes.add(new OfferCode("OFR001", null, 200, null, 50, 10));
        context.offerCodes.add(new OfferCode("OFR002", 150, 200, 50, 80, 5));
        Factory.getProcess("cost").run(context);
        assertEquals(1, context.costItems.size());
        CostItem costItem = context.costItems.get(0);
        assertTrue(BigDecimal.valueOf(0).compareTo(costItem.discount) == 0);
        assertTrue(BigDecimal.valueOf(750).compareTo(costItem.totalCost) == 0);
    }

    @Test
    public void testRun_withoutOfferCode() {
        testRun_withoutDiscount(null);
    }

    @Test
    public void testRun_offerCodeNotFound() {
        testRun_withoutDiscount("OFR999");
    }

    @Test
    public void testRun_outOfRange() {
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
        Factory.getProcess("cost").run(context);
        assertEquals(6, context.costItems.size());
        CostItem costItem = context.costItems.get(0);
        assertTrue(BigDecimal.valueOf(0).compareTo(costItem.discount) == 0);
        assertTrue(BigDecimal.valueOf(3250).compareTo(costItem.totalCost) == 0);
        costItem = context.costItems.get(1);
        assertTrue(BigDecimal.valueOf(0).compareTo(costItem.discount) == 0);
        assertTrue(BigDecimal.valueOf(875).compareTo(costItem.totalCost) == 0);
        costItem = context.costItems.get(2);
        assertTrue(BigDecimal.valueOf(0).compareTo(costItem.discount) == 0);
        assertTrue(BigDecimal.valueOf(875).compareTo(costItem.totalCost) == 0);
        costItem = context.costItems.get(3);
        assertTrue(BigDecimal.valueOf(0).compareTo(costItem.discount) == 0);
        assertTrue(BigDecimal.valueOf(2425).compareTo(costItem.totalCost) == 0);
        costItem = context.costItems.get(4);
        assertTrue(BigDecimal.valueOf(0).compareTo(costItem.discount) == 0);
        assertTrue(BigDecimal.valueOf(1850).compareTo(costItem.totalCost) == 0);
        costItem = context.costItems.get(5);
        assertTrue(BigDecimal.valueOf(0).compareTo(costItem.discount) == 0);
        assertTrue(BigDecimal.valueOf(2125).compareTo(costItem.totalCost) == 0);
    }

    @Test
    public void testRun_offerCodeOFR001() {
        CostContext context = new CostContext();
        context.weightFactor = 10;
        context.distanceFactor = 5;
        context.baseDeliveryCost = new BigDecimal(100);
        context.noOfPackages = 1;
        context.shipmentItems.add(new ShipmentItem("PKG1", 100, 45, "OFR001"));
        context.offerCodes.add(new OfferCode("OFR001", null, 200, null, 50, 10));
        context.offerCodes.add(new OfferCode("OFR002", 150, 200, 50, 80, 5));
        Factory.getProcess("cost").run(context);
        assertEquals(1, context.costItems.size());
        CostItem costItem = context.costItems.get(0);
        assertTrue(BigDecimal.valueOf(132.50).compareTo(costItem.discount) == 0);
        assertTrue(BigDecimal.valueOf(1192.50).compareTo(costItem.totalCost) == 0);

    }

    @Test
    public void testRun_offerCodeOFR002() {
        CostContext context = new CostContext();
        context.weightFactor = 10;
        context.distanceFactor = 5;
        context.baseDeliveryCost = new BigDecimal(100);
        context.noOfPackages = 1;
        context.shipmentItems.add(new ShipmentItem("PKG1", 155, 55, "OFR002"));
        context.offerCodes.add(new OfferCode("OFR001", null, 200, null, 50, 10));
        context.offerCodes.add(new OfferCode("OFR002", 150, 200, 50, 80, 5));
        Factory.getProcess("cost").run(context);
        assertEquals(1, context.costItems.size());
        CostItem costItem = context.costItems.get(0);
        assertTrue(BigDecimal.valueOf(96.25).compareTo(costItem.discount) == 0);
        assertTrue(BigDecimal.valueOf(1828.75).compareTo(costItem.totalCost) == 0);

    }
}

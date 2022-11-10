package com.everest.courier;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.Assert.*;

public class TestCostProcess {
    private void assertDiscountAndTotalCost(ResultItem item, float discount, float totalCost) {
        assertTrue(BigDecimal.valueOf(discount).compareTo(item.discount) == 0);
        assertTrue(BigDecimal.valueOf(totalCost).compareTo(item.totalCost) == 0);
    }

    private void testCostProcess_withoutDiscount(String strOfferCode) {
        Context context = new Context();
        context.type = ServiceType.COST;
        context.weightFactor = 10;
        context.distanceFactor = 5;
        context.baseDeliveryCost = new BigDecimal(100);
        context.noOfPackages = 1;
        context.shipmentItems.add(new ShipmentItem("PKG1", 50, 30, strOfferCode));
        context.offerCodes.add(new OfferCode("OFR001", null, 200, null, 50, 10));
        context.offerCodes.add(new OfferCode("OFR002", 150, 200, 50, 80, 5));
        new Factory().getProcess(ServiceType.COST).run(context);
        assertEquals(1, context.resultItems.size());
        ResultItem item = context.resultItems.get(0);
        assertDiscountAndTotalCost(item, 0, 750);
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
        Context context = new Context();
        context.type = ServiceType.COST;
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
        ResultItem item = context.resultItems.get(0);
        assertDiscountAndTotalCost(item, 0, 3250);
        item = context.resultItems.get(1);
        assertDiscountAndTotalCost(item, 0, 875);
        item = context.resultItems.get(2);
        assertDiscountAndTotalCost(item, 0, 875);
        item = context.resultItems.get(3);
        assertDiscountAndTotalCost(item, 0, 2425);
        item = context.resultItems.get(4);
        assertDiscountAndTotalCost(item, 0, 1850);
        item = context.resultItems.get(5);
        assertDiscountAndTotalCost(item, 0, 2125);
    }

    @Test
    public void testCostProcess_offerCodeOFR001() {
        Context context = new Context();
        context.type = ServiceType.COST;
        context.weightFactor = 10;
        context.distanceFactor = 5;
        context.baseDeliveryCost = new BigDecimal(100);
        context.noOfPackages = 1;
        context.shipmentItems.add(new ShipmentItem("PKG1", 100, 45, "OFR001"));
        context.offerCodes.add(new OfferCode("OFR001", null, 200, null, 50, 10));
        context.offerCodes.add(new OfferCode("OFR002", 150, 200, 50, 80, 5));
        new Factory().getProcess(ServiceType.COST).run(context);
        assertEquals(1, context.resultItems.size());
        ResultItem item = context.resultItems.get(0);
        assertDiscountAndTotalCost(item, 132.50f, 1192.50f);

    }

    @Test
    public void testCostProcess_offerCodeOFR002() {
        Context context = new Context();
        context.type = ServiceType.COST;
        context.weightFactor = 10;
        context.distanceFactor = 5;
        context.baseDeliveryCost = new BigDecimal(100);
        context.noOfPackages = 1;
        context.shipmentItems.add(new ShipmentItem("PKG1", 155, 55, "OFR002"));
        context.offerCodes.add(new OfferCode("OFR001", null, 200, null, 50, 10));
        context.offerCodes.add(new OfferCode("OFR002", 150, 200, 50, 80, 5));
        new Factory().getProcess(ServiceType.COST).run(context);
        assertEquals(1, context.resultItems.size());
        ResultItem item = context.resultItems.get(0);
        assertDiscountAndTotalCost(item, 96.25f, 1828.75f);

    }
}

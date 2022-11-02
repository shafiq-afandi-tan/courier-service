package com.everest.courier;

import com.everest.courier.Exceptions.MyException;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestParser {
    private void testParseCostContext_invalidInput(List<String> lines, String msg) {
        MyException e = assertThrows(MyException.class, ()->{
            Parser.parseCostContext(lines);
        });
        assertEquals(msg, e.getMessage());
    }

    @Test
    public void testParseCostContext_emptyLine() {
        List<String> lines = new ArrayList<>();
        lines.add("");
        testParseCostContext_invalidInput(lines, "Missing field - package_id");
    }

    @Test
    public void testParseCostContext_invalidBaseDeliveryCost() {
        List<String> lines = new ArrayList<>();
        lines.add("Junk");
        testParseCostContext_invalidInput(lines, "Invalid field - base_delivery_cost");
    }

    @Test
    public void testParseCostContext_missingField_noOfPackages() {
        List<String> lines = new ArrayList<>();
        lines.add("100.00");
        testParseCostContext_invalidInput(lines, "Missing field - no_of_packages");
    }

    @Test
    public void testParseCostContext_missingPackageInfo() {
        List<String> lines = new ArrayList<>();
        lines.add("100.00 1");
        testParseCostContext_invalidInput(lines, "Missing field - package_info");
    }

    @Test
    public void testParseCostContext_missingPackageWeigth() {
        List<String> lines = new ArrayList<>();
        lines.add("100.00 1");
        lines.add("PKG1");
        testParseCostContext_invalidInput(lines, "Missing field - package_weight");
    }

    @Test
    public void testParseCostContext_invalidPackageWeigth() {
        List<String> lines = new ArrayList<>();
        lines.add("100.00 1");
        lines.add("PKG1 JUNK");
        testParseCostContext_invalidInput(lines, "Invalid field - package_weight");
    }

    @Test
    public void testParseCostContext_missingDistance() {
        List<String> lines = new ArrayList<>();
        lines.add("100.00 1");
        lines.add("PKG1 50");
        testParseCostContext_invalidInput(lines, "Missing field - distance");
    }

    @Test
    public void testParseCostContext_invalidDistance() {
        List<String> lines = new ArrayList<>();
        lines.add("100.00 1");
        lines.add("PKG1 50 JUNK");
        testParseCostContext_invalidInput(lines, "Invalid field - distance");
    }

    private CostContext testParseCostContext_withoutOfferCodeHelper(List<String> lines) {
        try {
            CostContext context = Parser.parseCostContext(lines);
            assertTrue((new BigDecimal(100)).compareTo(context.baseDeliveryCost) == 0);
            assertEquals(1, context.noOfPackages);
            assertEquals(1, context.shipmentItems.size());
            ShipmentItem item = context.shipmentItems.get(0);
            assertEquals("PKG1", item.packageId);
            assertEquals(50, item.weight);
            assertEquals(30, item.distance);
            return context;
        }
        catch(Exception e) {
            fail("Should not throw exception");
        }
        return null;
    }

    @Test
    public void testParseCostContext_withoutOfferCode() {
        List<String> lines = new ArrayList<>();
        lines.add("100.00 1");
        lines.add("PKG1 50 30");
        testParseCostContext_withoutOfferCodeHelper(lines);
    }

    @Test
    public void testParseCostContext_withOfferCode() {
        List<String> lines = new ArrayList<>();
        lines.add("100.00 1");
        lines.add("PKG1 50 30 OFR001");
        CostContext context = testParseCostContext_withoutOfferCodeHelper(lines);
        assertEquals("OFR001", context.shipmentItems.get(0).offerCode);
    }

}

package com.everest.courier;

import com.everest.courier.Exceptions.ConfigurationException;
import com.everest.courier.Exceptions.GeneralException;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestParser {
    private void testParseCostContext_invalidInput(List<String> lines, String msg) {
        GeneralException e = assertThrows(GeneralException.class, ()->{
            new Parser().parseCostContext(lines);
        });
        assertEquals(msg, e.getMessage());
    }

    @Test
    public void testParseCostContext_missingBaseDeliveryCost() {
        List<String> lines = new ArrayList<>();
        lines.add("");
        testParseCostContext_invalidInput(lines, "Missing field - base_delivery_cost");
    }

    @Test
    public void testParseCostContext_invalidBaseDeliveryCost() {
        List<String> lines = new ArrayList<>();
        lines.add("Junk");
        testParseCostContext_invalidInput(lines, "Invalid field - base_delivery_cost");
    }

    @Test
    public void testParseCostContext_missingNoOfPackages() {
        List<String> lines = new ArrayList<>();
        lines.add("100.00");
        testParseCostContext_invalidInput(lines, "Missing field - no_of_packages");
    }

    @Test
    public void testParseCostContext_missingPackageId() {
        List<String> lines = new ArrayList<>();
        lines.add("100.00 1");
        testParseCostContext_invalidInput(lines, "Missing field - package_id");
    }

    @Test
    public void testParseCostContext_missingPackageWeight() {
        List<String> lines = new ArrayList<>();
        lines.add("100.00 1");
        lines.add("PKG1");
        testParseCostContext_invalidInput(lines, "Missing field - package_weight");
    }

    @Test
    public void testParseCostContext_invalidPackageWeight() {
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

    private Context testParseCostContext_withoutOfferCodeHelper(List<String> lines) {
        try {
            Context context = new Parser().parseCostContext(lines);
            assertEquals(ServiceType.COST, context.type);
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
        Context context = testParseCostContext_withoutOfferCodeHelper(lines);
        assertEquals("OFR001", context.shipmentItems.get(0).offerCode);
    }

    private void testParseTimeContext_invalidInput(List<String> lines, String msg) {
        GeneralException e = assertThrows(GeneralException.class, ()->{
            new Parser().parseTimeContext(lines);
        });
        assertEquals(msg, e.getMessage());
    }

    @Test
    public void testParseTimeContext_missingNoOfVehicle() {
        List<String> lines = new ArrayList<>();
        lines.add("100.00 1");
        lines.add("PKG1 100 50");
        testParseTimeContext_invalidInput(lines, "Missing field - no_of_vehicle");
    }

    @Test
    public void testParseTimeContext_emptyLine() {
        List<String> lines = new ArrayList<>();
        lines.add("100.00 1");
        lines.add("PKG1 100 50");
        lines.add("");
        testParseTimeContext_invalidInput(lines, "Missing field - no_of_vehicle");
    }

    @Test
    public void testParseTimeContext_invalidNoOfVehicle() {
        List<String> lines = new ArrayList<>();
        lines.add("100.00 1");
        lines.add("PKG1 100 50");
        lines.add("JUNK");
        testParseTimeContext_invalidInput(lines, "Invalid field - no_of_vehicle");
    }

    @Test
    public void testParseTimeContext_missingVehicleSpeed() {
        List<String> lines = new ArrayList<>();
        lines.add("100.00 1");
        lines.add("PKG1 100 50");
        lines.add("1");
        testParseTimeContext_invalidInput(lines, "Missing field - vehicle_speed");
    }

    @Test
    public void testParseTimeContext_invalidVehicleSpeed() {
        List<String> lines = new ArrayList<>();
        lines.add("100.00 1");
        lines.add("PKG1 100 50");
        lines.add("1 JUNK");
        testParseTimeContext_invalidInput(lines, "Invalid field - vehicle_speed");
    }

    @Test
    public void testParseTimeContext_missingVehicleCapacity() {
        List<String> lines = new ArrayList<>();
        lines.add("100.00 1");
        lines.add("PKG1 100 50");
        lines.add("1 70");
        testParseTimeContext_invalidInput(lines, "Missing field - vehicle_capacity");
    }

    @Test
    public void testParseTimeContext_invalidVehicleCapacity() {
        List<String> lines = new ArrayList<>();
        lines.add("100.00 1");
        lines.add("PKG1 100 50");
        lines.add("1 70 JUNK");
        testParseTimeContext_invalidInput(lines, "Invalid field - vehicle_capacity");
    }

    @Test
    public void testParseTimeContext() throws GeneralException {
        List<String> lines = new ArrayList<>();
        lines.add("100.00 1");
        lines.add("PKG1 100 50");
        lines.add("1 70 200");
        Context context = new Parser().parseTimeContext(lines);
        assertEquals(ServiceType.TIME, context.type);
        assertEquals(1, context.vehicles.size());
        ShippingVehicle item = context.vehicles.get(0);
        assertEquals("01", item.vehicleNo);
        assertEquals(70, item.speed);
        assertEquals(200, item.capacity);
        assertTrue(BigDecimal.valueOf(0).compareTo(item.availableAfter) == 0);
    }

    private void testBuildConfiguration_parseException(String strConfig) {
        Context context = new Context();
        ConfigurationException e = assertThrows(ConfigurationException.class, ()->{
            new Parser().buildConfiguration(context, strConfig);
        });
        Throwable cause = e.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof ParseException);
    }

    private void testBuildConfiguration_formatException(String strConfig) {
        Context context = new Context();
        ConfigurationException e = assertThrows(ConfigurationException.class, ()->{
            new Parser().buildConfiguration(context, strConfig);
        });
        Throwable cause = e.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof NumberFormatException);
    }

    public void testBuildConfiguration_configException(String strConfig) {
        Context context = new Context();
        ConfigurationException e = assertThrows(ConfigurationException.class, ()->{
            new Parser().buildConfiguration(context, strConfig);
        });
        assertNotNull(e);
    }

    @Test
    public void testBuildConfiguration_invalidOfferCode_negativeValue() {
        //test against negative value
        String strConfig = "{\n" +
                "  \"weightFactor\": 10,\n" +
                "  \"distanceFactor\": 5,\n" +
                "  \"offerCodes\": [\n" +
                "    {\"code\": \"OFR001\", \"weightLowerLimit\": -70, \"weightUpperLimit\":  200, \"distanceLowerLimit\":  null, \"distanceUpperLimit\":  200, \"discount\": 10},\n" +
                "  ]\n" +
                "}";
        testBuildConfiguration_configException(strConfig);
    }

    @Test
    public void testBuildConfiguration_invalidOfferCode_lowerHigher() {
        String strConfig = "{\n" +
                "  \"weightFactor\": 10,\n" +
                "  \"distanceFactor\": 5,\n" +
                "  \"offerCodes\": [\n" +
                "    {\"code\": \"OFR001\", \"weightLowerLimit\": 200, \"weightUpperLimit\":  70, \"distanceLowerLimit\":  null, \"distanceUpperLimit\":  200, \"discount\": 10},\n" +
                "  ]\n" +
                "}";
        testBuildConfiguration_configException(strConfig);
    }
}

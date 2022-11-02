package com.everest.courier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class CostContext {
    public BigDecimal baseDeliveryCost;
    public int noOfPackages;
    public List<ShipmentItem> shipmentItems = new ArrayList<>();

    public List<OfferCode> offerCodes = new ArrayList<>();

    public List<CostItem> costItems = new ArrayList<>();

    public int weightFactor;

    public int distanceFactor;

    public String name;
}

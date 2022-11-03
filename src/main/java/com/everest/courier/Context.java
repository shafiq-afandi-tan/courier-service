package com.everest.courier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Context {
    public BigDecimal baseDeliveryCost;
    public int noOfPackages;
    public List<ShipmentItem> shipmentItems = new ArrayList<>();

    public List<OfferCode> offerCodes = new ArrayList<>();

    public List<ResultItem> resultItems = new ArrayList<>();

    public int weightFactor;

    public int distanceFactor;

    public ServiceType type;
}

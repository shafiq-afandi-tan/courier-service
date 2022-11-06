package com.everest.courier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Context {
    public BigDecimal baseDeliveryCost;
    public int noOfPackages;
    public List<ShipmentItem> shipmentItems = new ArrayList<>();

    public List<OfferCode> offerCodes = new ArrayList<>();

    public List<ResultItem> resultItems = new ArrayList<>();

    public Map<String,ResultItem> resultMap = new HashMap<>();

    public int weightFactor;

    public int distanceFactor;

    public ServiceType type;
}

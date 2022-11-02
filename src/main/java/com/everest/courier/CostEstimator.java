package com.everest.courier;

import java.math.BigDecimal;
import java.util.*;

public class CostEstimator {
    private BigDecimal baseDeliveryCost;
    private int weightFactor;
    private int distanceFactor;
    private Map<String,OfferCode> offerCodes = new HashMap<>();

    public CostEstimator(BigDecimal baseDeliveryCost, int weightFactor, int distanceFactor, OfferCode[] offerCodes) {
        this.baseDeliveryCost = baseDeliveryCost;
        this.weightFactor = weightFactor;
        this.distanceFactor = distanceFactor;
        for(OfferCode offerCode: offerCodes)
            this.offerCodes.put(offerCode.code, offerCode);
    }

    public BigDecimal calculateDeliveryCost(ShipmentItem item) {
        return baseDeliveryCost.add(BigDecimal.valueOf(item.weight).multiply(BigDecimal.valueOf(weightFactor)))
                .add(BigDecimal.valueOf(item.distance).multiply(BigDecimal.valueOf(distanceFactor)));
    }

    public BigDecimal calculateDiscount(ShipmentItem item, BigDecimal total) {
        OfferCode offerCode = offerCodes.get(item.offerCode);
        boolean doApply = false;
        if(offerCode != null) {
            doApply = true;
            if(offerCode.weightLowerLimit != null && item.weight < offerCode.weightLowerLimit
                || offerCode.weightUpperLimit != null && item.weight > offerCode.weightUpperLimit
                || offerCode.distanceLowerLimit != null && item.distance < offerCode.distanceLowerLimit
                || offerCode.distanceUpperLimit != null && item.distance > offerCode.distanceUpperLimit)
                doApply = false;
        }
        if(doApply == true)
            return total.multiply(BigDecimal.valueOf(offerCode.discountPercent).divide(BigDecimal.valueOf(100)));
        else
            return BigDecimal.valueOf(0);
    }
}

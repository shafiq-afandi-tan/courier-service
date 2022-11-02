package com.everest.courier;

public class OfferCode {
    public String code;
    public Integer weightLowerLimit;
    public Integer weightUpperLimit;
    public Integer distanceLowerLimit;
    public Integer distanceUpperLimit;
    public int discountPercent;

    public OfferCode(String code, Integer weightLowerLimit, Integer weightUpperLimit, Integer distanceLowerLimit, Integer distanceUpperLimit, int discountPercent) {
        this.code = code;
        this.weightLowerLimit = weightLowerLimit;
        this.weightUpperLimit = weightUpperLimit;
        this.distanceLowerLimit = distanceLowerLimit;
        this.distanceUpperLimit = distanceUpperLimit;
        this.discountPercent = discountPercent;
    }

    public OfferCode() {
    }
}

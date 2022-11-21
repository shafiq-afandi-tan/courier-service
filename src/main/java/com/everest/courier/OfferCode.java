package com.everest.courier;

public class OfferCode {
    public String code;
    public Integer weightLowerLimit;
    public Integer weightUpperLimit;
    public Integer distanceLowerLimit;
    public Integer distanceUpperLimit;
    public Integer discountPercent;

    public String error = null;

    public OfferCode(String code, Integer weightLowerLimit, Integer weightUpperLimit, Integer distanceLowerLimit, Integer distanceUpperLimit, Integer discountPercent) {
        this.code = code;
        this.weightLowerLimit = weightLowerLimit;
        this.weightUpperLimit = weightUpperLimit;
        this.distanceLowerLimit = distanceLowerLimit;
        this.distanceUpperLimit = distanceUpperLimit;
        this.discountPercent = discountPercent;
    }

    public OfferCode() {
    }

    public boolean isValid() {
        if(this.weightLowerLimit != null && this.weightLowerLimit < 0) {
            this.error = "Invalid offer code - weight lower limit: " + this.weightLowerLimit;
            return false;
        }
        if(this.weightUpperLimit != null && this.weightUpperLimit < 0) {
            this.error = "Invalid offer code - weight upper limit: " + this.weightUpperLimit;
            return false;
        }
        if(this.weightLowerLimit != null && this.weightUpperLimit != null && this.weightLowerLimit > this.weightUpperLimit) {
            this.error = "Invalid offer code - weight lower limit is greater than upper limit";
            return false;
        }
        if(this.distanceLowerLimit != null && this.distanceLowerLimit < 0) {
            this.error = "Invalid offer code - distance lower limit: " + this.distanceLowerLimit;
            return false;
        }
        if(this.distanceUpperLimit != null && this.distanceUpperLimit < 0) {
            this.error = "Invalid offer code - distance upper limit: " + this.distanceUpperLimit;
            return false;
        }
        if(this.distanceLowerLimit != null && this.distanceUpperLimit != null && this.distanceLowerLimit > this.distanceUpperLimit) {
            this.error = "Invalid offer code - distance lower limit is greater than upper limit";
            return false;
        }
        if(this.discountPercent != null && this.discountPercent < 0) {
            this.error = "Invalid offer code - discount percentage: " + this.discountPercent;
            return false;
        }
        return true;
    }
}

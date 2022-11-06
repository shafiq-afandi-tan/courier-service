package com.everest.courier;

import java.math.BigDecimal;

public class TimeItem extends CostItem {
    public BigDecimal arrivalTime;

    public TimeItem(String packageId, BigDecimal discount, BigDecimal totalCost, BigDecimal arrivalTime) {
        super(packageId, discount, totalCost);
        this.arrivalTime = arrivalTime;
    }
}

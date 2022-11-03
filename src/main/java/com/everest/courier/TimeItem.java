package com.everest.courier;

import java.math.BigDecimal;

public class TimeItem extends CostItem {
    public BigDecimal deliveryTime;

    public TimeItem(String packageId, BigDecimal discount, BigDecimal totalCost, BigDecimal deliveryTime) {
        super(packageId, discount, totalCost);
        this.deliveryTime = deliveryTime;
    }
}

package com.everest.courier;

import java.math.BigDecimal;

public class ResultItem {
    public String packageId;
    public BigDecimal discount;
    public BigDecimal totalCost;
    public BigDecimal arrivalTime;

    public ResultItem(String packageId, BigDecimal discount, BigDecimal totalCost) {
        this.packageId = packageId;
        this.discount = discount;
        this.totalCost = totalCost;
    }
    public ResultItem(String packageId, BigDecimal discount, BigDecimal totalCost, BigDecimal arrivalTime) {
        this(packageId, discount, totalCost);
        this.arrivalTime = arrivalTime;
    }
}

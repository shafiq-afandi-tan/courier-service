package com.everest.courier;

import java.math.BigDecimal;

public class CostItem {
    public String packageId;
    public BigDecimal discount;
    public BigDecimal totalCost;

    public CostItem(String packageId, BigDecimal discount, BigDecimal totalCost) {
        this.packageId = packageId;
        this.discount = discount;
        this.totalCost = totalCost;
    }
}

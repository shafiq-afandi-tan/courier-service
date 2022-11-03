package com.everest.courier;

import java.math.BigDecimal;

public class ResultItem {
    public String packageId;
    public BigDecimal discount;
    public BigDecimal totalCost;

    public ResultItem(String packageId, BigDecimal discount, BigDecimal totalCost) {
        this.packageId = packageId;
        this.discount = discount;
        this.totalCost = totalCost;
    }
}

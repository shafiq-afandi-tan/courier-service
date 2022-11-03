package com.everest.courier;

import java.math.BigDecimal;

public class CostItem extends ResultItem {

    public CostItem(String packageId, BigDecimal discount, BigDecimal totalCost) {
        super(packageId, discount, totalCost);
    }
}

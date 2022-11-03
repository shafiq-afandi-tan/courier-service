package com.everest.courier;

import java.math.BigDecimal;
import java.util.List;

public class CostProcess implements Process {
    @Override
    public void run(Context argVal) {
        CostContext context = (CostContext)argVal;
        for(int i = 0; i < context.noOfPackages; i++) {
            ShipmentItem shipItem = context.shipmentItems.get(i);
            List<OfferCode> offerCodes = context.offerCodes;
            int size = offerCodes.size();
            CostEstimator estimator = new CostEstimator(
                    context.baseDeliveryCost,
                    context.weightFactor,
                    context.distanceFactor,
                    offerCodes.toArray(new OfferCode[size]));
            BigDecimal total = estimator.calculateDeliveryCost(shipItem);
            BigDecimal discount = estimator.calculateDiscount(shipItem, total);
            CostItem costItem = new CostItem(shipItem.packageId, discount, total.subtract(discount));
            context.resultItems.add(costItem);
        }
    }
}

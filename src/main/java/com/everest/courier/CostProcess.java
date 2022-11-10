package com.everest.courier;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CostProcess implements Process {
    private Factory factory;

    public CostProcess(Factory factory) {
        this.factory = factory;
    }

    @Override
    public void run(Context context) {
        for(int i = 0; i < context.noOfPackages; i++) {
            ShipmentItem shipItem = context.shipmentItems.get(i);
            //Calculate delivery cost
            BigDecimal deliveryCost = context.baseDeliveryCost.add(BigDecimal.valueOf(shipItem.weight).multiply(BigDecimal.valueOf(context.weightFactor)))
                    .add(BigDecimal.valueOf(shipItem.distance).multiply(BigDecimal.valueOf(context.distanceFactor)));
            //Calculate its discount
            BigDecimal discount = BigDecimal.valueOf(0);
            Map<String,OfferCode> offerCodeMap = new HashMap<String,OfferCode>();
            for(OfferCode offerCode: context.offerCodes)
                offerCodeMap.put(offerCode.code, offerCode);
            OfferCode offerCode = offerCodeMap.get(shipItem.offerCode);
            boolean doApply = false;
            if(offerCode != null) {
                doApply = true;
                if(offerCode.weightLowerLimit != null && shipItem.weight < offerCode.weightLowerLimit
                        || offerCode.weightUpperLimit != null && shipItem.weight > offerCode.weightUpperLimit
                        || offerCode.distanceLowerLimit != null && shipItem.distance < offerCode.distanceLowerLimit
                        || offerCode.distanceUpperLimit != null && shipItem.distance > offerCode.distanceUpperLimit)
                    doApply = false;
            }
            if(doApply == true)
                discount = deliveryCost.multiply(BigDecimal.valueOf(offerCode.discountPercent).divide(BigDecimal.valueOf(100)));
            //Work out result
            ResultItem resultItem = factory.getResultItem(shipItem.packageId, discount, deliveryCost.subtract(discount));
            context.resultItems.add(resultItem);
            context.resultMap.put(resultItem.packageId, resultItem);
        }
    }
}

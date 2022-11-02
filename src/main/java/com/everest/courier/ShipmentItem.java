package com.everest.courier;

public class ShipmentItem {
    public String packageId;
    public int weight;
    public int distance;
    public String offerCode;

    public ShipmentItem(String packageId, int weight, int distance, String offerCode) {
        this.packageId = packageId;
        this.weight = weight;
        this.distance = distance;
        this.offerCode = offerCode;
    }

    public ShipmentItem() {
    }
}

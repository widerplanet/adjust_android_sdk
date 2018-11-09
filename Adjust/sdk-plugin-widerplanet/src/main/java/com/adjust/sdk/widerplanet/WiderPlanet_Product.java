package com.adjust.sdk.plugin;

/**
 * Created by Wider Planet on 21/12/17.
 */
public class WiderPlanetProduct {
    float price;
    int quantity;
    String product_id;
    public WiderPlanetProduct(float price, int quantity, String product_id) {
        this.price = price;
        this.quantity = quantity;
        this.productID = product_id;
    }
}
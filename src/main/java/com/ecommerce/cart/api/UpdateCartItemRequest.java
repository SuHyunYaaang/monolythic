package com.ecommerce.cart.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class UpdateCartItemRequest {
    
    @NotBlank
    private String skuId;
    
    @Min(1)
    private int quantity;
    
    protected UpdateCartItemRequest() {
        // JSON deserialization
    }
    
    public UpdateCartItemRequest(String skuId, int quantity) {
        this.skuId = skuId;
        this.quantity = quantity;
    }
    
    public String getSkuId() {
        return skuId;
    }
    
    public int getQuantity() {
        return quantity;
    }
}




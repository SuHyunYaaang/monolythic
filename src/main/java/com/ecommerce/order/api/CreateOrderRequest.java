package com.ecommerce.order.api;

import jakarta.validation.constraints.NotBlank;

public class CreateOrderRequest {
    
    @NotBlank
    private String shippingAddress;
    
    @NotBlank
    private String billingAddress;
    
    private String notes;
    
    protected CreateOrderRequest() {
        // JSON deserialization
    }
    
    public CreateOrderRequest(String shippingAddress, String billingAddress, String notes) {
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.notes = notes;
    }
    
    public String getShippingAddress() {
        return shippingAddress;
    }
    
    public String getBillingAddress() {
        return billingAddress;
    }
    
    public String getNotes() {
        return notes;
    }
}




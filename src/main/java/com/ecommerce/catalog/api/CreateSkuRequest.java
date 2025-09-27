package com.ecommerce.catalog.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class CreateSkuRequest {
    
    @NotBlank
    @Size(max = 100)
    private String skuCode;
    
    @Size(max = 200)
    private String name;
    
    @Size(max = 1000)
    private String description;
    
    @NotBlank
    private String productId;
    
    @NotNull
    private BigDecimal price;
    
    @NotBlank
    private String currency;
    
    protected CreateSkuRequest() {
        // JSON deserialization
    }
    
    public CreateSkuRequest(String skuCode, String name, String description, String productId, BigDecimal price, String currency) {
        this.skuCode = skuCode;
        this.name = name;
        this.description = description;
        this.productId = productId;
        this.price = price;
        this.currency = currency;
    }
    
    public String getSkuCode() {
        return skuCode;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getProductId() {
        return productId;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public String getCurrency() {
        return currency;
    }
}




package com.ecommerce.catalog.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class CreateProductRequest {
    
    @NotBlank
    @Size(max = 200)
    private String name;
    
    @Size(max = 1000)
    private String description;
    
    @NotBlank
    private String categoryId;
    
    @NotNull
    private BigDecimal basePrice;
    
    @NotBlank
    private String currency;
    
    protected CreateProductRequest() {
        // JSON deserialization
    }
    
    public CreateProductRequest(String name, String description, String categoryId, BigDecimal basePrice, String currency) {
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.basePrice = basePrice;
        this.currency = currency;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getCategoryId() {
        return categoryId;
    }
    
    public BigDecimal getBasePrice() {
        return basePrice;
    }
    
    public String getCurrency() {
        return currency;
    }
}




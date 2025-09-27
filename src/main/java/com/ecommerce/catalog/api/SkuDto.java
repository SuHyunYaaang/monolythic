package com.ecommerce.catalog.api;

import com.ecommerce.catalog.domain.Sku;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SkuDto {
    
    private String skuId;
    private String skuCode;
    private String name;
    private String description;
    private String productId;
    private BigDecimal price;
    private String currency;
    private int stockQuantity;
    private int reservedQuantity;
    private int availableQuantity;
    private boolean active;
    private boolean trackInventory;
    private int minOrderQuantity;
    private int maxOrderQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    protected SkuDto() {
        // JSON deserialization
    }
    
    public SkuDto(String skuId, String skuCode, String name, String description, String productId, 
                  BigDecimal price, String currency, int stockQuantity, int reservedQuantity, 
                  int availableQuantity, boolean active, boolean trackInventory, 
                  int minOrderQuantity, int maxOrderQuantity, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.skuId = skuId;
        this.skuCode = skuCode;
        this.name = name;
        this.description = description;
        this.productId = productId;
        this.price = price;
        this.currency = currency;
        this.stockQuantity = stockQuantity;
        this.reservedQuantity = reservedQuantity;
        this.availableQuantity = availableQuantity;
        this.active = active;
        this.trackInventory = trackInventory;
        this.minOrderQuantity = minOrderQuantity;
        this.maxOrderQuantity = maxOrderQuantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public static SkuDto from(Sku sku) {
        return new SkuDto(
                sku.getSkuId().getValue(),
                sku.getSkuCode(),
                sku.getName(),
                sku.getDescription(),
                sku.getProduct() != null ? sku.getProduct().getProductId().getValue() : null,
                sku.getPrice().getAmount(),
                sku.getPrice().getCurrency(),
                sku.getStockQuantity(),
                sku.getReservedQuantity(),
                sku.getAvailableQuantity(),
                sku.isActive(),
                sku.isTrackInventory(),
                sku.getMinOrderQuantity(),
                sku.getMaxOrderQuantity(),
                sku.getCreatedAt(),
                sku.getUpdatedAt()
        );
    }
    
    public String getSkuId() {
        return skuId;
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
    
    public int getStockQuantity() {
        return stockQuantity;
    }
    
    public int getReservedQuantity() {
        return reservedQuantity;
    }
    
    public int getAvailableQuantity() {
        return availableQuantity;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public boolean isTrackInventory() {
        return trackInventory;
    }
    
    public int getMinOrderQuantity() {
        return minOrderQuantity;
    }
    
    public int getMaxOrderQuantity() {
        return maxOrderQuantity;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}




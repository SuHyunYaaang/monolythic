package com.ecommerce.order.api;

import com.ecommerce.order.domain.OrderItem;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemDto {
    
    private String skuId;
    private String skuCode;
    private String productName;
    private String skuName;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    protected OrderItemDto() {
        // JSON deserialization
    }
    
    public OrderItemDto(String skuId, String skuCode, String productName, String skuName,
                       int quantity, BigDecimal unitPrice, BigDecimal totalPrice, String currency,
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.skuId = skuId;
        this.skuCode = skuCode;
        this.productName = productName;
        this.skuName = skuName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.currency = currency;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public static OrderItemDto from(OrderItem orderItem) {
        return new OrderItemDto(
                orderItem.getSkuId().getValue(),
                orderItem.getSkuCode(),
                orderItem.getProductName(),
                orderItem.getSkuName(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice().getAmount(),
                orderItem.getTotalPrice().getAmount(),
                orderItem.getUnitPrice().getCurrency(),
                orderItem.getCreatedAt(),
                orderItem.getUpdatedAt()
        );
    }
    
    public String getSkuId() {
        return skuId;
    }
    
    public String getSkuCode() {
        return skuCode;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public String getSkuName() {
        return skuName;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}




package com.ecommerce.cart.api;

import com.ecommerce.cart.domain.CartItem;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemDto {
    
    private String skuId;
    private int quantity;
    private BigDecimal unitPrice;
    private String currency;
    private BigDecimal subtotal;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    protected CartItemDto() {
        // JSON deserialization
    }
    
    public CartItemDto(String skuId, int quantity, BigDecimal unitPrice, String currency, 
                      BigDecimal subtotal, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.skuId = skuId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.currency = currency;
        this.subtotal = subtotal;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public static CartItemDto from(CartItem cartItem) {
        return new CartItemDto(
                cartItem.getSkuId().getValue(),
                cartItem.getQuantity(),
                cartItem.getUnitPrice().getAmount(),
                cartItem.getUnitPrice().getCurrency(),
                cartItem.calculateSubtotal().getAmount(),
                cartItem.getCreatedAt(),
                cartItem.getUpdatedAt()
        );
    }
    
    public String getSkuId() {
        return skuId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}




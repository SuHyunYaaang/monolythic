package com.ecommerce.cart.api;

import com.ecommerce.cart.domain.Cart;
import com.ecommerce.cart.domain.CartItem;
import com.ecommerce.shared.domain.Money;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartDto {
    
    private String cartId;
    private String customerId;
    private List<CartItemDto> items;
    private BigDecimal totalAmount;
    private String currency;
    private int totalItemCount;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    protected CartDto() {
        // JSON deserialization
    }
    
    public CartDto(String cartId, String customerId, List<CartItemDto> items, 
                   BigDecimal totalAmount, String currency, int totalItemCount, 
                   boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.cartId = cartId;
        this.customerId = customerId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.totalItemCount = totalItemCount;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public static CartDto from(Cart cart) {
        List<CartItemDto> itemDtos = cart.getItems().stream()
                .map(CartItemDto::from)
                .collect(Collectors.toList());
        
        Money total = cart.calculateTotal();
        
        return new CartDto(
                cart.getCartId().getValue(),
                cart.getCustomerId().getValue(),
                itemDtos,
                total.getAmount(),
                total.getCurrency(),
                cart.getTotalItemCount(),
                cart.isActive(),
                cart.getCreatedAt(),
                cart.getUpdatedAt()
        );
    }
    
    public String getCartId() {
        return cartId;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public List<CartItemDto> getItems() {
        return items;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public int getTotalItemCount() {
        return totalItemCount;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}




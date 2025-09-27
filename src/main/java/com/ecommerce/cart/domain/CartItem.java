package com.ecommerce.cart.domain;

import com.ecommerce.shared.domain.BaseEntity;
import com.ecommerce.shared.domain.EntityId;
import com.ecommerce.shared.domain.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "cart_items")
public class CartItem extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "sku_id", nullable = false))
    private EntityId skuId;
    
    @Min(1)
    @Column(nullable = false)
    private int quantity;
    
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "unit_price", nullable = false))
    @AttributeOverride(name = "currency", column = @Column(name = "currency", nullable = false))
    private Money unitPrice;
    
    protected CartItem() {
        // JPA
    }
    
    public CartItem(Cart cart, EntityId skuId, int quantity) {
        this.cart = cart;
        this.skuId = skuId;
        this.quantity = quantity;
        this.unitPrice = Money.zero(); // Will be set by service layer
    }
    
    public void updateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.quantity = quantity;
    }
    
    public void updateUnitPrice(Money unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public Money calculateSubtotal() {
        return unitPrice.multiply(quantity);
    }
    
    public Cart getCart() {
        return cart;
    }
    
    public EntityId getSkuId() {
        return skuId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public Money getUnitPrice() {
        return unitPrice;
    }
}


package com.ecommerce.cart.domain;

import com.ecommerce.shared.domain.BaseEntity;
import com.ecommerce.shared.domain.EntityId;
import com.ecommerce.shared.domain.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "carts")
public class Cart extends BaseEntity {
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "cart_id", unique = true, nullable = false))
    private EntityId cartId;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "customer_id", nullable = false))
    private EntityId customerId;
    
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();
    
    @Column(nullable = false)
    private boolean active = true;
    
    protected Cart() {
        // JPA
    }
    
    public Cart(EntityId customerId) {
        this.cartId = EntityId.generate();
        this.customerId = customerId;
    }
    
    public void addItem(EntityId skuId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        Optional<CartItem> existingItem = findItemBySkuId(skuId);
        if (existingItem.isPresent()) {
            existingItem.get().updateQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem(this, skuId, quantity);
            this.items.add(newItem);
        }
    }
    
    public void updateItemQuantity(EntityId skuId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        Optional<CartItem> existingItem = findItemBySkuId(skuId);
        if (existingItem.isPresent()) {
            existingItem.get().updateQuantity(quantity);
        } else {
            throw new IllegalArgumentException("Item not found in cart");
        }
    }
    
    public void removeItem(EntityId skuId) {
        Optional<CartItem> item = findItemBySkuId(skuId);
        if (item.isPresent()) {
            this.items.remove(item.get());
        }
    }
    
    public void clear() {
        this.items.clear();
    }
    
    public void deactivate() {
        this.active = false;
    }
    
    public Money calculateTotal() {
        return items.stream()
                .map(CartItem::calculateSubtotal)
                .reduce(Money.zero(), Money::add);
    }
    
    public int getTotalItemCount() {
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    private Optional<CartItem> findItemBySkuId(EntityId skuId) {
        return items.stream()
                .filter(item -> item.getSkuId().equals(skuId))
                .findFirst();
    }
    
    public EntityId getCartId() {
        return cartId;
    }
    
    public EntityId getCustomerId() {
        return customerId;
    }
    
    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }
    
    public boolean isActive() {
        return active;
    }
}


package com.ecommerce.catalog.domain;

import com.ecommerce.shared.domain.BaseEntity;
import com.ecommerce.shared.domain.EntityId;
import com.ecommerce.shared.domain.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "skus")
public class Sku extends BaseEntity {
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "sku_id", unique = true, nullable = false))
    private EntityId skuId;
    
    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String skuCode;
    
    @Size(max = 200)
    private String name;
    
    @Size(max = 1000)
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "price", nullable = false))
    @AttributeOverride(name = "currency", column = @Column(name = "currency", nullable = false))
    private Money price;
    
    @Column(nullable = false)
    private int stockQuantity = 0;
    
    @Column(nullable = false)
    private int reservedQuantity = 0;
    
    @Column(nullable = false)
    private boolean active = true;
    
    @Column(nullable = false)
    private boolean trackInventory = true;
    
    @Column(nullable = false)
    private int minOrderQuantity = 1;
    
    @Column(nullable = false)
    private int maxOrderQuantity = 999;
    
    protected Sku() {
        // JPA
    }
    
    public Sku(String skuCode, String name, String description, Product product, Money price) {
        this.skuId = EntityId.generate();
        this.skuCode = skuCode;
        this.name = name;
        this.description = description;
        this.product = product;
        this.price = price;
    }
    
    public void updateName(String name) {
        this.name = name;
    }
    
    public void updateDescription(String description) {
        this.description = description;
    }
    
    public void updatePrice(Money price) {
        this.price = price;
    }
    
    public void updateStockQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        this.stockQuantity = quantity;
    }
    
    public void reserveStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Reserve quantity must be positive");
        }
        if (getAvailableQuantity() < quantity) {
            throw new IllegalStateException("Insufficient stock available");
        }
        this.reservedQuantity += quantity;
    }
    
    public void releaseReservedStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Release quantity must be positive");
        }
        if (this.reservedQuantity < quantity) {
            throw new IllegalStateException("Cannot release more than reserved quantity");
        }
        this.reservedQuantity -= quantity;
    }
    
    public void consumeReservedStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Consume quantity must be positive");
        }
        if (this.reservedQuantity < quantity) {
            throw new IllegalStateException("Cannot consume more than reserved quantity");
        }
        this.reservedQuantity -= quantity;
        this.stockQuantity -= quantity;
    }
    
    public void activate() {
        this.active = true;
    }
    
    public void deactivate() {
        this.active = false;
    }
    
    public void setTrackInventory(boolean trackInventory) {
        this.trackInventory = trackInventory;
    }
    
    public void updateMinOrderQuantity(int minOrderQuantity) {
        if (minOrderQuantity < 1) {
            throw new IllegalArgumentException("Min order quantity must be at least 1");
        }
        this.minOrderQuantity = minOrderQuantity;
    }
    
    public void updateMaxOrderQuantity(int maxOrderQuantity) {
        if (maxOrderQuantity < 1) {
            throw new IllegalArgumentException("Max order quantity must be at least 1");
        }
        this.maxOrderQuantity = maxOrderQuantity;
    }
    
    public boolean isInStock() {
        return !trackInventory || getAvailableQuantity() > 0;
    }
    
    public int getAvailableQuantity() {
        return stockQuantity - reservedQuantity;
    }
    
    public boolean canFulfillQuantity(int quantity) {
        return !trackInventory || getAvailableQuantity() >= quantity;
    }
    
    public EntityId getSkuId() {
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
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public Money getPrice() {
        return price;
    }
    
    public int getStockQuantity() {
        return stockQuantity;
    }
    
    public int getReservedQuantity() {
        return reservedQuantity;
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
}


package com.ecommerce.order.domain;

import com.ecommerce.shared.domain.BaseEntity;
import com.ecommerce.shared.domain.EntityId;
import com.ecommerce.shared.domain.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "order_items")
public class OrderItem extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "sku_id", nullable = false))
    private EntityId skuId;
    
    @Column(nullable = false)
    private String skuCode;
    
    @Column(nullable = false)
    private String productName;
    
    @Column(nullable = false)
    private String skuName;
    
    @Min(1)
    @Column(nullable = false)
    private int quantity;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "unit_price", nullable = false)),
        @AttributeOverride(name = "currency", column = @Column(name = "currency", nullable = false))
    })
    private Money unitPrice;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "total_price", nullable = false)),
        @AttributeOverride(name = "currency", column = @Column(name = "currency", insertable = false, updatable = false))
    })
    private Money totalPrice;
    
    protected OrderItem() {
        // JPA
    }
    
    public OrderItem(EntityId skuId, String skuCode, String productName, String skuName, 
                     int quantity, Money unitPrice) {
        this.skuId = skuId;
        this.skuCode = skuCode;
        this.productName = productName;
        this.skuName = skuName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice.multiply(quantity);
    }
    
    public void setOrder(Order order) {
        this.order = order;
    }
    
    public Order getOrder() {
        return order;
    }
    
    public EntityId getSkuId() {
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
    
    public Money getUnitPrice() {
        return unitPrice;
    }
    
    public Money getTotalPrice() {
        return totalPrice;
    }
}




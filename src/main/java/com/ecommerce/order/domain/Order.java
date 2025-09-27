package com.ecommerce.order.domain;

import com.ecommerce.shared.domain.BaseEntity;
import com.ecommerce.shared.domain.EntityId;
import com.ecommerce.shared.domain.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "order_id", unique = true, nullable = false))
    private EntityId orderId;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "customer_id", nullable = false))
    private EntityId customerId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "subtotal", nullable = false)),
        @AttributeOverride(name = "currency", column = @Column(name = "currency", nullable = false))
    })
    private Money subtotal;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "tax_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "currency", insertable = false, updatable = false))
    })
    private Money taxAmount;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "shipping_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "currency", insertable = false, updatable = false))
    })
    private Money shippingAmount;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "total_amount", nullable = false)),
        @AttributeOverride(name = "currency", column = @Column(name = "currency", insertable = false, updatable = false))
    })
    private Money totalAmount;
    
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;
    
    @Column(name = "shipping_address")
    private String shippingAddress;
    
    @Column(name = "billing_address")
    private String billingAddress;
    
    @Column(name = "notes")
    private String notes;
    
    protected Order() {
        // JPA
    }
    
    public Order(EntityId customerId, List<OrderItem> items, Money subtotal, 
                 Money taxAmount, Money shippingAmount, String shippingAddress, 
                 String billingAddress, String notes) {
        this.orderId = EntityId.generate();
        this.customerId = customerId;
        this.items = new ArrayList<>(items);
        this.subtotal = subtotal;
        this.taxAmount = taxAmount;
        this.shippingAmount = shippingAmount;
        this.totalAmount = calculateTotal();
        this.orderDate = LocalDateTime.now();
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.notes = notes;
        
        // Set order reference for items
        this.items.forEach(item -> item.setOrder(this));
    }
    
    public void confirm() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Only pending orders can be confirmed");
        }
        this.status = OrderStatus.CONFIRMED;
    }
    
    public void process() {
        if (status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Only confirmed orders can be processed");
        }
        this.status = OrderStatus.PROCESSING;
    }
    
    public void ship() {
        if (status != OrderStatus.PROCESSING) {
            throw new IllegalStateException("Only processing orders can be shipped");
        }
        this.status = OrderStatus.SHIPPED;
    }
    
    public void deliver() {
        if (status != OrderStatus.SHIPPED) {
            throw new IllegalStateException("Only shipped orders can be delivered");
        }
        this.status = OrderStatus.DELIVERED;
    }
    
    public void cancel() {
        if (status == OrderStatus.DELIVERED || status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel delivered or already cancelled orders");
        }
        this.status = OrderStatus.CANCELLED;
    }
    
    public void refund() {
        if (status != OrderStatus.DELIVERED) {
            throw new IllegalStateException("Only delivered orders can be refunded");
        }
        this.status = OrderStatus.REFUNDED;
    }
    
    public void updateShippingAddress(String shippingAddress) {
        if (status != OrderStatus.PENDING && status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Cannot update shipping address for orders in current status");
        }
        this.shippingAddress = shippingAddress;
    }
    
    public void updateNotes(String notes) {
        this.notes = notes;
    }
    
    private Money calculateTotal() {
        Money total = subtotal;
        if (taxAmount != null) {
            total = total.add(taxAmount);
        }
        if (shippingAmount != null) {
            total = total.add(shippingAmount);
        }
        return total;
    }
    
    public boolean canBeCancelled() {
        return status == OrderStatus.PENDING || status == OrderStatus.CONFIRMED || status == OrderStatus.PROCESSING;
    }
    
    public boolean isCompleted() {
        return status == OrderStatus.DELIVERED || status == OrderStatus.REFUNDED;
    }
    
    public EntityId getOrderId() {
        return orderId;
    }
    
    public EntityId getCustomerId() {
        return customerId;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }
    
    public Money getSubtotal() {
        return subtotal;
    }
    
    public Money getTaxAmount() {
        return taxAmount;
    }
    
    public Money getShippingAmount() {
        return shippingAmount;
    }
    
    public Money getTotalAmount() {
        return totalAmount;
    }
    
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    
    public String getShippingAddress() {
        return shippingAddress;
    }
    
    public String getBillingAddress() {
        return billingAddress;
    }
    
    public String getNotes() {
        return notes;
    }
}


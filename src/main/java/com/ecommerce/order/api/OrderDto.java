package com.ecommerce.order.api;

import com.ecommerce.order.domain.Order;
import com.ecommerce.order.domain.OrderItem;
import com.ecommerce.order.domain.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto {
    
    private String orderId;
    private String customerId;
    private OrderStatus status;
    private String statusDescription;
    private List<OrderItemDto> items;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal shippingAmount;
    private BigDecimal totalAmount;
    private String currency;
    private LocalDateTime orderDate;
    private String shippingAddress;
    private String billingAddress;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    protected OrderDto() {
        // JSON deserialization
    }
    
    public OrderDto(String orderId, String customerId, OrderStatus status, String statusDescription,
                   List<OrderItemDto> items, BigDecimal subtotal, BigDecimal taxAmount, 
                   BigDecimal shippingAmount, BigDecimal totalAmount, String currency,
                   LocalDateTime orderDate, String shippingAddress, String billingAddress, 
                   String notes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.status = status;
        this.statusDescription = statusDescription;
        this.items = items;
        this.subtotal = subtotal;
        this.taxAmount = taxAmount;
        this.shippingAmount = shippingAmount;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.orderDate = orderDate;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public static OrderDto from(Order order) {
        List<OrderItemDto> itemDtos = order.getItems().stream()
                .map(OrderItemDto::from)
                .collect(Collectors.toList());
        
        return new OrderDto(
                order.getOrderId().getValue(),
                order.getCustomerId().getValue(),
                order.getStatus(),
                order.getStatus().getDescription(),
                itemDtos,
                order.getSubtotal().getAmount(),
                order.getTaxAmount() != null ? order.getTaxAmount().getAmount() : null,
                order.getShippingAmount() != null ? order.getShippingAmount().getAmount() : null,
                order.getTotalAmount().getAmount(),
                order.getTotalAmount().getCurrency(),
                order.getOrderDate(),
                order.getShippingAddress(),
                order.getBillingAddress(),
                order.getNotes(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public String getStatusDescription() {
        return statusDescription;
    }
    
    public List<OrderItemDto> getItems() {
        return items;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }
    
    public BigDecimal getShippingAmount() {
        return shippingAmount;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public String getCurrency() {
        return currency;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}




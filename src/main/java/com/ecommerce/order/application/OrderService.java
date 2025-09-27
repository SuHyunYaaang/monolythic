package com.ecommerce.order.application;

import com.ecommerce.cart.application.CartService;
import com.ecommerce.cart.domain.Cart;
import com.ecommerce.cart.domain.CartItem;
import com.ecommerce.catalog.application.CatalogService;
import com.ecommerce.catalog.domain.Sku;
import com.ecommerce.order.domain.*;
import com.ecommerce.shared.domain.EntityId;
import com.ecommerce.shared.domain.Money;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final CatalogService catalogService;
    
    public OrderService(OrderRepository orderRepository, 
                       CartService cartService, 
                       CatalogService catalogService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.catalogService = catalogService;
    }
    
    @Transactional(readOnly = true)
    public Optional<Order> getOrder(EntityId orderId) {
        return orderRepository.findByOrderId(orderId);
    }
    
    @Transactional(readOnly = true)
    public Optional<Order> getOrderWithItems(EntityId orderId) {
        return orderRepository.findByOrderIdWithItems(orderId);
    }
    
    @Transactional(readOnly = true)
    public Page<Order> getOrdersByCustomer(EntityId customerId, Pageable pageable) {
        return orderRepository.findByCustomerIdOrderByOrderDateDesc(customerId, pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<Order> getOrdersByCustomerAndStatus(EntityId customerId, OrderStatus status, Pageable pageable) {
        return orderRepository.findByCustomerIdAndStatusOrderByOrderDateDesc(customerId, status, pageable);
    }
    
    public Order createOrderFromCart(EntityId customerId, String shippingAddress, 
                                   String billingAddress, String notes) {
        // Get customer's cart
        Cart cart = cartService.getCart(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found or empty"));
        
        if (cart.isEmpty()) {
            throw new IllegalArgumentException("Cannot create order from empty cart");
        }
        
        // Validate all items and reserve stock
        List<OrderItem> orderItems = new ArrayList<>();
        Money subtotal = Money.zero();
        
        for (CartItem cartItem : cart.getItems()) {
            Sku sku = catalogService.getSku(cartItem.getSkuId())
                    .orElseThrow(() -> new IllegalArgumentException("SKU not found: " + cartItem.getSkuId()));
            
            if (!sku.isActive()) {
                throw new IllegalArgumentException("SKU is not active: " + sku.getSkuCode());
            }
            
            if (!sku.canFulfillQuantity(cartItem.getQuantity())) {
                throw new IllegalArgumentException("Insufficient stock for SKU: " + sku.getSkuCode());
            }
            
            // Reserve stock
            catalogService.reserveStock(sku.getSkuId(), cartItem.getQuantity());
            
            // Create order item
            OrderItem orderItem = new OrderItem(
                    sku.getSkuId(),
                    sku.getSkuCode(),
                    sku.getProduct().getName(),
                    sku.getName(),
                    cartItem.getQuantity(),
                    sku.getPrice()
            );
            
            orderItems.add(orderItem);
            subtotal = subtotal.add(orderItem.getTotalPrice());
        }
        
        // Calculate tax and shipping (simplified)
        Money taxAmount = calculateTax(subtotal);
        Money shippingAmount = calculateShipping(subtotal);
        
        // Create order
        Order order = new Order(
                customerId,
                orderItems,
                subtotal,
                taxAmount,
                shippingAmount,
                shippingAddress,
                billingAddress,
                notes
        );
        
        Order savedOrder = orderRepository.save(order);
        
        // Deactivate cart after successful order creation
        cartService.deactivateCart(customerId);
        
        return savedOrder;
    }
    
    public Order confirmOrder(EntityId orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        
        order.confirm();
        
        return orderRepository.save(order);
    }
    
    public Order processOrder(EntityId orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        
        order.process();
        
        return orderRepository.save(order);
    }
    
    public Order shipOrder(EntityId orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        
        order.ship();
        
        return orderRepository.save(order);
    }
    
    public Order deliverOrder(EntityId orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        
        order.deliver();
        
        return orderRepository.save(order);
    }
    
    public Order cancelOrder(EntityId orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        
        if (!order.canBeCancelled()) {
            throw new IllegalStateException("Order cannot be cancelled in current status");
        }
        
        // Release reserved stock
        for (OrderItem item : order.getItems()) {
            catalogService.releaseReservedStock(item.getSkuId(), item.getQuantity());
        }
        
        order.cancel();
        
        return orderRepository.save(order);
    }
    
    public Order refundOrder(EntityId orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        
        order.refund();
        
        return orderRepository.save(order);
    }
    
    public Order updateOrderShippingAddress(EntityId orderId, String shippingAddress) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        
        order.updateShippingAddress(shippingAddress);
        
        return orderRepository.save(order);
    }
    
    public Order updateOrderNotes(EntityId orderId, String notes) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        
        order.updateNotes(notes);
        
        return orderRepository.save(order);
    }
    
    private Money calculateTax(Money subtotal) {
        // Simplified tax calculation - 10% tax rate
        return subtotal.multiply(BigDecimal.valueOf(0.10));
    }
    
    private Money calculateShipping(Money subtotal) {
        // Simplified shipping calculation - free shipping over $50
        if (subtotal.isGreaterThan(Money.of(BigDecimal.valueOf(50)))) {
            return Money.zero();
        }
        return Money.of(BigDecimal.valueOf(10)); // $10 shipping
    }
}




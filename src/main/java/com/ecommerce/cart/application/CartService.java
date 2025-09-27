package com.ecommerce.cart.application;

import com.ecommerce.cart.domain.Cart;
import com.ecommerce.cart.domain.CartItem;
import com.ecommerce.cart.domain.CartRepository;
import com.ecommerce.catalog.application.CatalogService;
import com.ecommerce.catalog.domain.Sku;
import com.ecommerce.shared.domain.EntityId;
import com.ecommerce.shared.domain.Money;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CartService {
    
    private final CartRepository cartRepository;
    private final CatalogService catalogService;
    
    public CartService(CartRepository cartRepository, CatalogService catalogService) {
        this.cartRepository = cartRepository;
        this.catalogService = catalogService;
    }
    
    @Transactional(readOnly = true)
    @Cacheable(value = "carts", key = "#customerId.value")
    public Optional<Cart> getCart(EntityId customerId) {
        return cartRepository.findByCustomerIdWithItems(customerId);
    }
    
    @Transactional(readOnly = true)
    public Optional<Cart> getCartByCartId(EntityId cartId) {
        return cartRepository.findByCartId(cartId);
    }
    
    @CacheEvict(value = "carts", key = "#customerId.value")
    public Cart addItemToCart(EntityId customerId, EntityId skuId, int quantity) {
        // Validate SKU exists and is available
        Sku sku = catalogService.getSku(skuId)
                .orElseThrow(() -> new IllegalArgumentException("SKU not found"));
        
        if (!sku.isActive()) {
            throw new IllegalArgumentException("SKU is not active");
        }
        
        if (!sku.canFulfillQuantity(quantity)) {
            throw new IllegalArgumentException("Insufficient stock available");
        }
        
        // Get or create cart
        Cart cart = cartRepository.findByCustomerIdAndActiveTrue(customerId)
                .orElseGet(() -> {
                    Cart newCart = new Cart(customerId);
                    return cartRepository.save(newCart);
                });
        
        // Add item to cart
        cart.addItem(skuId, quantity);
        
        // Update cart item prices
        updateCartItemPrices(cart);
        
        return cartRepository.save(cart);
    }
    
    @CacheEvict(value = "carts", key = "#customerId.value")
    public Cart updateCartItemQuantity(EntityId customerId, EntityId skuId, int quantity) {
        Cart cart = cartRepository.findByCustomerIdWithItems(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        
        // Validate SKU availability if increasing quantity
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getSkuId().equals(skuId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found in cart"));
        
        if (quantity > existingItem.getQuantity()) {
            Sku sku = catalogService.getSku(skuId)
                    .orElseThrow(() -> new IllegalArgumentException("SKU not found"));
            
            int additionalQuantity = quantity - existingItem.getQuantity();
            if (!sku.canFulfillQuantity(additionalQuantity)) {
                throw new IllegalArgumentException("Insufficient stock available");
            }
        }
        
        cart.updateItemQuantity(skuId, quantity);
        updateCartItemPrices(cart);
        
        return cartRepository.save(cart);
    }
    
    @CacheEvict(value = "carts", key = "#customerId.value")
    public Cart removeItemFromCart(EntityId customerId, EntityId skuId) {
        Cart cart = cartRepository.findByCustomerIdWithItems(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        
        cart.removeItem(skuId);
        
        return cartRepository.save(cart);
    }
    
    @CacheEvict(value = "carts", key = "#customerId.value")
    public Cart clearCart(EntityId customerId) {
        Cart cart = cartRepository.findByCustomerIdWithItems(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        
        cart.clear();
        
        return cartRepository.save(cart);
    }
    
    @CacheEvict(value = "carts", key = "#customerId.value")
    public void deactivateCart(EntityId customerId) {
        Cart cart = cartRepository.findByCustomerIdAndActiveTrue(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        
        cart.deactivate();
        cartRepository.save(cart);
    }
    
    private void updateCartItemPrices(Cart cart) {
        for (CartItem item : cart.getItems()) {
            Sku sku = catalogService.getSku(item.getSkuId())
                    .orElseThrow(() -> new IllegalArgumentException("SKU not found"));
            
            item.updateUnitPrice(sku.getPrice());
        }
    }
}




package com.ecommerce.cart.api;

import com.ecommerce.cart.application.CartService;
import com.ecommerce.cart.domain.Cart;
import com.ecommerce.shared.api.ApiResponse;
import com.ecommerce.shared.domain.EntityId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@Tag(name = "Cart", description = "장바구니 관리 API")
public class CartController {
    
    private final CartService cartService;
    
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    
    @GetMapping("/{customerId}")
    @Operation(summary = "장바구니 조회", description = "고객의 장바구니를 조회합니다.")
    public ResponseEntity<ApiResponse<CartDto>> getCart(
            @Parameter(description = "고객 ID") @PathVariable String customerId) {
        
        return cartService.getCart(EntityId.of(customerId))
                .map(cart -> ResponseEntity.ok(ApiResponse.success(CartDto.from(cart))))
                .orElse(ResponseEntity.ok(ApiResponse.success("장바구니가 비어있습니다.", null)));
    }
    
    @PostMapping("/{customerId}/items")
    @Operation(summary = "장바구니에 상품 추가", description = "장바구니에 상품을 추가합니다.")
    public ResponseEntity<ApiResponse<CartDto>> addToCart(
            @Parameter(description = "고객 ID") @PathVariable String customerId,
            @Valid @RequestBody AddToCartRequest request) {
        
        Cart cart = cartService.addItemToCart(
                EntityId.of(customerId),
                EntityId.of(request.getSkuId()),
                request.getQuantity()
        );
        
        return ResponseEntity.ok(ApiResponse.success("상품이 장바구니에 추가되었습니다.", CartDto.from(cart)));
    }
    
    @PutMapping("/{customerId}/items")
    @Operation(summary = "장바구니 상품 수량 변경", description = "장바구니에 있는 상품의 수량을 변경합니다.")
    public ResponseEntity<ApiResponse<CartDto>> updateCartItem(
            @Parameter(description = "고객 ID") @PathVariable String customerId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        
        Cart cart = cartService.updateCartItemQuantity(
                EntityId.of(customerId),
                EntityId.of(request.getSkuId()),
                request.getQuantity()
        );
        
        return ResponseEntity.ok(ApiResponse.success("장바구니 상품 수량이 변경되었습니다.", CartDto.from(cart)));
    }
    
    @DeleteMapping("/{customerId}/items/{skuId}")
    @Operation(summary = "장바구니에서 상품 제거", description = "장바구니에서 특정 상품을 제거합니다.")
    public ResponseEntity<ApiResponse<CartDto>> removeFromCart(
            @Parameter(description = "고객 ID") @PathVariable String customerId,
            @Parameter(description = "SKU ID") @PathVariable String skuId) {
        
        Cart cart = cartService.removeItemFromCart(
                EntityId.of(customerId),
                EntityId.of(skuId)
        );
        
        return ResponseEntity.ok(ApiResponse.success("상품이 장바구니에서 제거되었습니다.", CartDto.from(cart)));
    }
    
    @DeleteMapping("/{customerId}")
    @Operation(summary = "장바구니 비우기", description = "장바구니의 모든 상품을 제거합니다.")
    public ResponseEntity<ApiResponse<CartDto>> clearCart(
            @Parameter(description = "고객 ID") @PathVariable String customerId) {
        
        Cart cart = cartService.clearCart(EntityId.of(customerId));
        
        return ResponseEntity.ok(ApiResponse.success("장바구니가 비워졌습니다.", CartDto.from(cart)));
    }
}




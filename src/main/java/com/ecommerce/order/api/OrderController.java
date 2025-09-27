package com.ecommerce.order.api;

import com.ecommerce.order.application.OrderService;
import com.ecommerce.order.domain.Order;
import com.ecommerce.order.domain.OrderStatus;
import com.ecommerce.shared.api.ApiResponse;
import com.ecommerce.shared.domain.EntityId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@Tag(name = "Order", description = "주문 관리 API")
public class OrderController {
    
    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @GetMapping("/{orderId}")
    @Operation(summary = "주문 상세 조회", description = "특정 주문의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<OrderDto>> getOrder(
            @Parameter(description = "주문 ID") @PathVariable String orderId) {
        
        return orderService.getOrderWithItems(EntityId.of(orderId))
                .map(order -> ResponseEntity.ok(ApiResponse.success(OrderDto.from(order))))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "고객 주문 목록 조회", description = "특정 고객의 주문 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<Page<OrderDto>>> getOrdersByCustomer(
            @Parameter(description = "고객 ID") @PathVariable String customerId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<Order> orders = orderService.getOrdersByCustomer(EntityId.of(customerId), pageable);
        Page<OrderDto> orderDtos = orders.map(OrderDto::from);
        
        return ResponseEntity.ok(ApiResponse.success(orderDtos));
    }
    
    @GetMapping("/customer/{customerId}/status/{status}")
    @Operation(summary = "고객 주문 상태별 조회", description = "특정 고객의 특정 상태 주문 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<Page<OrderDto>>> getOrdersByCustomerAndStatus(
            @Parameter(description = "고객 ID") @PathVariable String customerId,
            @Parameter(description = "주문 상태") @PathVariable OrderStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<Order> orders = orderService.getOrdersByCustomerAndStatus(EntityId.of(customerId), status, pageable);
        Page<OrderDto> orderDtos = orders.map(OrderDto::from);
        
        return ResponseEntity.ok(ApiResponse.success(orderDtos));
    }
    
    @PostMapping("/customer/{customerId}")
    @Operation(summary = "주문 생성", description = "장바구니에서 주문을 생성합니다.")
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(
            @Parameter(description = "고객 ID") @PathVariable String customerId,
            @Valid @RequestBody CreateOrderRequest request) {
        
        Order order = orderService.createOrderFromCart(
                EntityId.of(customerId),
                request.getShippingAddress(),
                request.getBillingAddress(),
                request.getNotes()
        );
        
        return ResponseEntity.ok(ApiResponse.success("주문이 생성되었습니다.", OrderDto.from(order)));
    }
    
    @PutMapping("/{orderId}/confirm")
    @Operation(summary = "주문 확인", description = "주문을 확인 상태로 변경합니다.")
    public ResponseEntity<ApiResponse<OrderDto>> confirmOrder(
            @Parameter(description = "주문 ID") @PathVariable String orderId) {
        
        Order order = orderService.confirmOrder(EntityId.of(orderId));
        
        return ResponseEntity.ok(ApiResponse.success("주문이 확인되었습니다.", OrderDto.from(order)));
    }
    
    @PutMapping("/{orderId}/process")
    @Operation(summary = "주문 처리", description = "주문을 처리 상태로 변경합니다.")
    public ResponseEntity<ApiResponse<OrderDto>> processOrder(
            @Parameter(description = "주문 ID") @PathVariable String orderId) {
        
        Order order = orderService.processOrder(EntityId.of(orderId));
        
        return ResponseEntity.ok(ApiResponse.success("주문이 처리되었습니다.", OrderDto.from(order)));
    }
    
    @PutMapping("/{orderId}/ship")
    @Operation(summary = "주문 배송", description = "주문을 배송 상태로 변경합니다.")
    public ResponseEntity<ApiResponse<OrderDto>> shipOrder(
            @Parameter(description = "주문 ID") @PathVariable String orderId) {
        
        Order order = orderService.shipOrder(EntityId.of(orderId));
        
        return ResponseEntity.ok(ApiResponse.success("주문이 배송되었습니다.", OrderDto.from(order)));
    }
    
    @PutMapping("/{orderId}/deliver")
    @Operation(summary = "주문 배송 완료", description = "주문을 배송 완료 상태로 변경합니다.")
    public ResponseEntity<ApiResponse<OrderDto>> deliverOrder(
            @Parameter(description = "주문 ID") @PathVariable String orderId) {
        
        Order order = orderService.deliverOrder(EntityId.of(orderId));
        
        return ResponseEntity.ok(ApiResponse.success("주문이 배송 완료되었습니다.", OrderDto.from(order)));
    }
    
    @PutMapping("/{orderId}/cancel")
    @Operation(summary = "주문 취소", description = "주문을 취소합니다.")
    public ResponseEntity<ApiResponse<OrderDto>> cancelOrder(
            @Parameter(description = "주문 ID") @PathVariable String orderId) {
        
        Order order = orderService.cancelOrder(EntityId.of(orderId));
        
        return ResponseEntity.ok(ApiResponse.success("주문이 취소되었습니다.", OrderDto.from(order)));
    }
    
    @PutMapping("/{orderId}/refund")
    @Operation(summary = "주문 환불", description = "주문을 환불 처리합니다.")
    public ResponseEntity<ApiResponse<OrderDto>> refundOrder(
            @Parameter(description = "주문 ID") @PathVariable String orderId) {
        
        Order order = orderService.refundOrder(EntityId.of(orderId));
        
        return ResponseEntity.ok(ApiResponse.success("주문이 환불되었습니다.", OrderDto.from(order)));
    }
}




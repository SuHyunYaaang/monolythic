package com.ecommerce.order.domain;

public enum OrderStatus {
    PENDING,
    CONFIRMED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    REFUNDED,
    RETURNED;
    
    public String getDescription() {
        switch (this) {
            case PENDING:
                return "주문 대기 중";
            case CONFIRMED:
                return "주문 확인됨";
            case PROCESSING:
                return "처리 중";
            case SHIPPED:
                return "배송 중";
            case DELIVERED:
                return "배송 완료";
            case CANCELLED:
                return "주문 취소됨";
            case REFUNDED:
                return "환불됨";
            case RETURNED:
                return "반품됨";
            default:
                return this.name();
        }
    }
}

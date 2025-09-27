package com.ecommerce.order.domain;

import com.ecommerce.shared.domain.EntityId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    Optional<Order> findByOrderId(EntityId orderId);
    
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items WHERE o.orderId = :orderId")
    Optional<Order> findByOrderIdWithItems(@Param("orderId") EntityId orderId);
    
    @Query("SELECT o FROM Order o WHERE o.customerId = :customerId ORDER BY o.orderDate DESC")
    Page<Order> findByCustomerIdOrderByOrderDateDesc(@Param("customerId") EntityId customerId, Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.customerId = :customerId AND o.status = :status ORDER BY o.orderDate DESC")
    Page<Order> findByCustomerIdAndStatusOrderByOrderDateDesc(@Param("customerId") EntityId customerId, 
                                                              @Param("status") OrderStatus status, 
                                                              Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.status = :status ORDER BY o.orderDate ASC")
    List<Order> findByStatusOrderByOrderDateAsc(@Param("status") OrderStatus status);
    
    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate ORDER BY o.orderDate DESC")
    Page<Order> findByOrderDateBetween(@Param("startDate") LocalDateTime startDate, 
                                       @Param("endDate") LocalDateTime endDate, 
                                       Pageable pageable);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.customerId = :customerId AND o.status = :status")
    long countByCustomerIdAndStatus(@Param("customerId") EntityId customerId, @Param("status") OrderStatus status);
    
    boolean existsByOrderId(EntityId orderId);
}




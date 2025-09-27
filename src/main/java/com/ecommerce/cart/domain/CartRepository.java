package com.ecommerce.cart.domain;

import com.ecommerce.shared.domain.EntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    
    Optional<Cart> findByCartId(EntityId cartId);
    
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items ci LEFT JOIN FETCH ci.skuId WHERE c.customerId = :customerId AND c.active = true")
    Optional<Cart> findByCustomerIdWithItems(@Param("customerId") EntityId customerId);
    
    @Query("SELECT c FROM Cart c WHERE c.customerId = :customerId AND c.active = true")
    Optional<Cart> findByCustomerIdAndActiveTrue(@Param("customerId") EntityId customerId);
    
    boolean existsByCartId(EntityId cartId);
    
    boolean existsByCustomerIdAndActiveTrue(EntityId customerId);
}




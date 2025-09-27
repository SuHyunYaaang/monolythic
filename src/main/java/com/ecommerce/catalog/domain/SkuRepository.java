package com.ecommerce.catalog.domain;

import com.ecommerce.shared.domain.EntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface SkuRepository extends JpaRepository<Sku, Long> {
    
    Optional<Sku> findBySkuId(EntityId skuId);
    
    Optional<Sku> findBySkuCode(String skuCode);
    
    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT s FROM Sku s WHERE s.skuId = :skuId AND s.active = true")
    Optional<Sku> findBySkuIdWithLock(@Param("skuId") EntityId skuId);
    
    @Query("SELECT s FROM Sku s WHERE s.product.productId = :productId AND s.active = true")
    List<Sku> findByProductIdAndActiveTrue(@Param("productId") EntityId productId);
    
    @Query("SELECT s FROM Sku s WHERE s.active = true AND s.trackInventory = true AND s.stockQuantity > 0")
    List<Sku> findInStockSkus();
    
    @Query("SELECT s FROM Sku s WHERE s.active = true AND s.trackInventory = true AND s.stockQuantity <= :threshold")
    List<Sku> findLowStockSkus(@Param("threshold") int threshold);
    
    boolean existsBySkuId(EntityId skuId);
    
    boolean existsBySkuCode(String skuCode);
}




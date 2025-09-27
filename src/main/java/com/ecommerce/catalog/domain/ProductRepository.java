package com.ecommerce.catalog.domain;

import com.ecommerce.shared.domain.EntityId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Optional<Product> findByProductId(EntityId productId);
    
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.categoryId = :categoryId")
    Page<Product> findByCategoryIdAndActiveTrue(@Param("categoryId") EntityId categoryId, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.active = true AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Product> findByKeywordAndActiveTrue(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.active = true")
    Page<Product> findActiveProducts(Pageable pageable);
    
    @Query("SELECT p FROM Product p JOIN FETCH p.skus s WHERE p.productId = :productId AND p.active = true")
    Optional<Product> findByProductIdWithSkus(@Param("productId") EntityId productId);
    
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.digital = :digital")
    Page<Product> findByDigitalAndActiveTrue(@Param("digital") boolean digital, Pageable pageable);
    
    boolean existsByProductId(EntityId productId);
}




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
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    Optional<Category> findByCategoryId(EntityId categoryId);
    
    List<Category> findByParentCategoryIdIsNullAndActiveTrueOrderBySortOrder();
    
    List<Category> findByParentCategoryIdAndActiveTrueOrderBySortOrder(EntityId parentCategoryId);
    
    @Query("SELECT c FROM Category c WHERE c.active = true AND " +
           "(LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Category> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT c FROM Category c WHERE c.active = true")
    Page<Category> findActiveCategories(Pageable pageable);
    
    boolean existsByCategoryId(EntityId categoryId);
    
    boolean existsByParentCategoryId(EntityId parentCategoryId);
}




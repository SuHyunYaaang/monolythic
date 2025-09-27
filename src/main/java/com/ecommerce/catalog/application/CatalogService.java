package com.ecommerce.catalog.application;

import com.ecommerce.catalog.domain.*;
import com.ecommerce.shared.domain.EntityId;
import com.ecommerce.shared.domain.Money;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CatalogService {
    
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final SkuRepository skuRepository;
    
    public CatalogService(CategoryRepository categoryRepository, 
                         ProductRepository productRepository, 
                         SkuRepository skuRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.skuRepository = skuRepository;
    }
    
    // Category operations
    @Transactional(readOnly = true)
    @Cacheable(value = "categories", key = "#categoryId.value")
    public Optional<Category> getCategory(EntityId categoryId) {
        return categoryRepository.findByCategoryId(categoryId);
    }
    
    @Transactional(readOnly = true)
    @Cacheable(value = "rootCategories")
    public List<Category> getRootCategories() {
        return categoryRepository.findByParentCategoryIdIsNullAndActiveTrueOrderBySortOrder();
    }
    
    @Transactional(readOnly = true)
    @Cacheable(value = "childCategories", key = "#parentCategoryId.value")
    public List<Category> getChildCategories(EntityId parentCategoryId) {
        return categoryRepository.findByParentCategoryIdAndActiveTrueOrderBySortOrder(parentCategoryId);
    }
    
    @Transactional(readOnly = true)
    public Page<Category> searchCategories(String keyword, Pageable pageable) {
        return categoryRepository.findByKeyword(keyword, pageable);
    }
    
    public Category createRootCategory(String name, String description) {
        Category category = Category.createRoot(name, description);
        return categoryRepository.save(category);
    }
    
    public Category createChildCategory(String name, String description, EntityId parentCategoryId) {
        if (!categoryRepository.existsByCategoryId(parentCategoryId)) {
            throw new IllegalArgumentException("Parent category not found");
        }
        Category category = Category.createChild(name, description, parentCategoryId);
        return categoryRepository.save(category);
    }
    
    public Category updateCategory(EntityId categoryId, String name, String description) {
        Category category = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        
        category.updateName(name);
        category.updateDescription(description);
        
        return categoryRepository.save(category);
    }
    
    public void deactivateCategory(EntityId categoryId) {
        Category category = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        
        category.deactivate();
        categoryRepository.save(category);
    }
    
    // Product operations
    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "#productId.value")
    public Optional<Product> getProduct(EntityId productId) {
        return productRepository.findByProductId(productId);
    }
    
    @Transactional(readOnly = true)
    @Cacheable(value = "productsWithSkus", key = "#productId.value")
    public Optional<Product> getProductWithSkus(EntityId productId) {
        return productRepository.findByProductIdWithSkus(productId);
    }
    
    @Transactional(readOnly = true)
    public Page<Product> getProductsByCategory(EntityId categoryId, Pageable pageable) {
        return productRepository.findByCategoryIdAndActiveTrue(categoryId, pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByKeywordAndActiveTrue(keyword, pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<Product> getAllActiveProducts(Pageable pageable) {
        return productRepository.findActiveProducts(pageable);
    }
    
    public Product createProduct(String name, String description, EntityId categoryId, Money basePrice) {
        if (!categoryRepository.existsByCategoryId(categoryId)) {
            throw new IllegalArgumentException("Category not found");
        }
        
        Product product = new Product(name, description, categoryId, basePrice);
        return productRepository.save(product);
    }
    
    public Product updateProduct(EntityId productId, String name, String description, Money basePrice) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        
        product.updateName(name);
        product.updateDescription(description);
        product.updateBasePrice(basePrice);
        
        return productRepository.save(product);
    }
    
    public void deactivateProduct(EntityId productId) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        
        product.deactivate();
        productRepository.save(product);
    }
    
    // SKU operations
    @Transactional(readOnly = true)
    @Cacheable(value = "skus", key = "#skuId.value")
    public Optional<Sku> getSku(EntityId skuId) {
        return skuRepository.findBySkuId(skuId);
    }
    
    @Transactional(readOnly = true)
    public Optional<Sku> getSkuByCode(String skuCode) {
        return skuRepository.findBySkuCode(skuCode);
    }
    
    @Transactional(readOnly = true)
    public List<Sku> getSkusByProduct(EntityId productId) {
        return skuRepository.findByProductIdAndActiveTrue(productId);
    }
    
    public Sku createSku(String skuCode, String name, String description, EntityId productId, Money price) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        
        if (skuRepository.existsBySkuCode(skuCode)) {
            throw new IllegalArgumentException("SKU code already exists");
        }
        
        Sku sku = new Sku(skuCode, name, description, product, price);
        product.addSku(sku);
        
        return skuRepository.save(sku);
    }
    
    public Sku updateSku(EntityId skuId, String name, String description, Money price) {
        Sku sku = skuRepository.findBySkuId(skuId)
                .orElseThrow(() -> new IllegalArgumentException("SKU not found"));
        
        sku.updateName(name);
        sku.updateDescription(description);
        sku.updatePrice(price);
        
        return skuRepository.save(sku);
    }
    
    public void updateStock(EntityId skuId, int quantity) {
        Sku sku = skuRepository.findBySkuId(skuId)
                .orElseThrow(() -> new IllegalArgumentException("SKU not found"));
        
        sku.updateStockQuantity(quantity);
        skuRepository.save(sku);
    }
    
    public void reserveStock(EntityId skuId, int quantity) {
        Sku sku = skuRepository.findBySkuIdWithLock(skuId)
                .orElseThrow(() -> new IllegalArgumentException("SKU not found"));
        
        sku.reserveStock(quantity);
        skuRepository.save(sku);
    }
    
    public void releaseReservedStock(EntityId skuId, int quantity) {
        Sku sku = skuRepository.findBySkuIdWithLock(skuId)
                .orElseThrow(() -> new IllegalArgumentException("SKU not found"));
        
        sku.releaseReservedStock(quantity);
        skuRepository.save(sku);
    }
    
    public void consumeReservedStock(EntityId skuId, int quantity) {
        Sku sku = skuRepository.findBySkuIdWithLock(skuId)
                .orElseThrow(() -> new IllegalArgumentException("SKU not found"));
        
        sku.consumeReservedStock(quantity);
        skuRepository.save(sku);
    }
}




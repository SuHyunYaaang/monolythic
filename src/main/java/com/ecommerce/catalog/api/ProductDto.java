package com.ecommerce.catalog.api;

import com.ecommerce.catalog.domain.Product;
import com.ecommerce.catalog.domain.Sku;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    
    private String productId;
    private String name;
    private String description;
    private String categoryId;
    private BigDecimal basePrice;
    private String currency;
    private boolean active;
    private boolean digital;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<SkuDto> skus;
    
    protected ProductDto() {
        // JSON deserialization
    }
    
    public ProductDto(String productId, String name, String description, String categoryId, 
                     BigDecimal basePrice, String currency, boolean active, boolean digital, 
                     String imageUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.basePrice = basePrice;
        this.currency = currency;
        this.active = active;
        this.digital = digital;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public static ProductDto from(Product product) {
        return new ProductDto(
                product.getProductId().getValue(),
                product.getName(),
                product.getDescription(),
                product.getCategoryId().getValue(),
                product.getBasePrice().getAmount(),
                product.getBasePrice().getCurrency(),
                product.isActive(),
                product.isDigital(),
                product.getImageUrl(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
    
    public static ProductDto fromWithSkus(Product product) {
        ProductDto dto = from(product);
        if (product.getSkus() != null && !product.getSkus().isEmpty()) {
            dto.skus = product.getSkus().stream()
                    .map(SkuDto::from)
                    .collect(Collectors.toList());
        }
        return dto;
    }
    
    public String getProductId() {
        return productId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getCategoryId() {
        return categoryId;
    }
    
    public BigDecimal getBasePrice() {
        return basePrice;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public boolean isDigital() {
        return digital;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public List<SkuDto> getSkus() {
        return skus;
    }
}




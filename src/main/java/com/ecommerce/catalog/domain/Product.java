package com.ecommerce.catalog.domain;

import com.ecommerce.shared.domain.BaseEntity;
import com.ecommerce.shared.domain.EntityId;
import com.ecommerce.shared.domain.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "product_id", unique = true, nullable = false))
    private EntityId productId;
    
    @NotBlank
    @Size(max = 200)
    @Column(nullable = false)
    private String name;
    
    @Size(max = 1000)
    private String description;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "category_id", nullable = false))
    private EntityId categoryId;
    
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "base_price", nullable = false))
    @AttributeOverride(name = "currency", column = @Column(name = "currency", nullable = false))
    private Money basePrice;
    
    @Column(nullable = false)
    private boolean active = true;
    
    @Column(nullable = false)
    private boolean digital = false;
    
    @Size(max = 500)
    private String imageUrl;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sku> skus = new ArrayList<>();
    
    protected Product() {
        // JPA
    }
    
    public Product(String name, String description, EntityId categoryId, Money basePrice) {
        this.productId = EntityId.generate();
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.basePrice = basePrice;
    }
    
    public void updateName(String name) {
        this.name = name;
    }
    
    public void updateDescription(String description) {
        this.description = description;
    }
    
    public void updateBasePrice(Money basePrice) {
        this.basePrice = basePrice;
    }
    
    public void activate() {
        this.active = true;
    }
    
    public void deactivate() {
        this.active = false;
    }
    
    public void setDigital(boolean digital) {
        this.digital = digital;
    }
    
    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public void addSku(Sku sku) {
        sku.setProduct(this);
        this.skus.add(sku);
    }
    
    public void removeSku(Sku sku) {
        this.skus.remove(sku);
        sku.setProduct(null);
    }
    
    public EntityId getProductId() {
        return productId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public EntityId getCategoryId() {
        return categoryId;
    }
    
    public Money getBasePrice() {
        return basePrice;
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
    
    public List<Sku> getSkus() {
        return Collections.unmodifiableList(skus);
    }
}


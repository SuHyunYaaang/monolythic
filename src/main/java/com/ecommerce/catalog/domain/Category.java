package com.ecommerce.catalog.domain;

import com.ecommerce.shared.domain.BaseEntity;
import com.ecommerce.shared.domain.EntityId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "category_id", unique = true, nullable = false))
    private EntityId categoryId;
    
    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String name;
    
    @Size(max = 500)
    private String description;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "parent_category_id"))
    private EntityId parentCategoryId;
    
    @Column(nullable = false)
    private boolean active = true;
    
    @Column(nullable = false)
    private int sortOrder = 0;
    
    protected Category() {
        // JPA
    }
    
    public Category(String name, String description, EntityId parentCategoryId) {
        this.categoryId = EntityId.generate();
        this.name = name;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
    }
    
    public static Category createRoot(String name, String description) {
        return new Category(name, description, null);
    }
    
    public static Category createChild(String name, String description, EntityId parentCategoryId) {
        return new Category(name, description, parentCategoryId);
    }
    
    public void updateName(String name) {
        this.name = name;
    }
    
    public void updateDescription(String description) {
        this.description = description;
    }
    
    public void activate() {
        this.active = true;
    }
    
    public void deactivate() {
        this.active = false;
    }
    
    public void updateSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public EntityId getCategoryId() {
        return categoryId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public EntityId getParentCategoryId() {
        return parentCategoryId;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public int getSortOrder() {
        return sortOrder;
    }
}


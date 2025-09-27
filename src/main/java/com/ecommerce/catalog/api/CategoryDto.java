package com.ecommerce.catalog.api;

import com.ecommerce.catalog.domain.Category;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDto {
    
    private String categoryId;
    
    @NotBlank
    @Size(max = 100)
    private String name;
    
    @Size(max = 500)
    private String description;
    
    private String parentCategoryId;
    private boolean active;
    private int sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CategoryDto> children;
    
    protected CategoryDto() {
        // JSON deserialization
    }
    
    public CategoryDto(String categoryId, String name, String description, String parentCategoryId, 
                      boolean active, int sortOrder, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
        this.active = active;
        this.sortOrder = sortOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public static CategoryDto from(Category category) {
        return new CategoryDto(
                category.getCategoryId().getValue(),
                category.getName(),
                category.getDescription(),
                category.getParentCategoryId() != null ? category.getParentCategoryId().getValue() : null,
                category.isActive(),
                category.getSortOrder(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
    
    public String getCategoryId() {
        return categoryId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getParentCategoryId() {
        return parentCategoryId;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public int getSortOrder() {
        return sortOrder;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public List<CategoryDto> getChildren() {
        return children;
    }
    
    public void setChildren(List<CategoryDto> children) {
        this.children = children;
    }
}




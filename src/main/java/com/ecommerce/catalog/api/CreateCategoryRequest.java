package com.ecommerce.catalog.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateCategoryRequest {
    
    @NotBlank
    @Size(max = 100)
    private String name;
    
    @Size(max = 500)
    private String description;
    
    private String parentCategoryId;
    
    protected CreateCategoryRequest() {
        // JSON deserialization
    }
    
    public CreateCategoryRequest(String name, String description, String parentCategoryId) {
        this.name = name;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
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
}




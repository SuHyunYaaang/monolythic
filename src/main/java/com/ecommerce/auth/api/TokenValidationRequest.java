package com.ecommerce.auth.api;

import jakarta.validation.constraints.NotBlank;

public class TokenValidationRequest {
    
    @NotBlank
    private String token;
    
    protected TokenValidationRequest() {
        // JSON deserialization
    }
    
    public TokenValidationRequest(String token) {
        this.token = token;
    }
    
    public String getToken() {
        return token;
    }
}




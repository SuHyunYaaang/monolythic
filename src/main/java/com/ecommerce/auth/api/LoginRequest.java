package com.ecommerce.auth.api;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    
    @NotBlank
    private String username;
    
    @NotBlank
    private String password;
    
    protected LoginRequest() {
        // JSON deserialization
    }
    
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
}




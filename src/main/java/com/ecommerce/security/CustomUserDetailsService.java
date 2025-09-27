package com.ecommerce.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private final PasswordEncoder passwordEncoder;
    
    public CustomUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // For demo purposes, we'll create a simple user
        // In a real application, you would load user details from a database
        
        if ("admin".equals(username)) {
            return new User(
                    "admin",
                    passwordEncoder.encode("admin123"),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
        } else if ("user".equals(username)) {
            return new User(
                    "user",
                    passwordEncoder.encode("user123"),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
        } else if ("customer1".equals(username)) {
            return new User(
                    "customer1",
                    passwordEncoder.encode("password123"),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
        }
        
        throw new UsernameNotFoundException("User not found: " + username);
    }
}




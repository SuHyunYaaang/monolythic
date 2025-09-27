package com.ecommerce.security;

import com.ecommerce.shared.api.ProblemDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtAccessDeniedHandler.class);
    
    private final ObjectMapper objectMapper;
    
    public JwtAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                      AccessDeniedException accessDeniedException) throws IOException {
        
        logger.error("Access denied error: {}", accessDeniedException.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.of(
                org.springframework.http.HttpStatus.FORBIDDEN,
                "Access Denied",
                "You do not have permission to access this resource"
        ).withExtension("path", request.getRequestURI());
        
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        
        objectMapper.writeValue(response.getOutputStream(), problemDetail);
    }
}




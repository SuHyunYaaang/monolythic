package com.ecommerce.shared.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemDetail {
    
    private URI type;
    private String title;
    private int status;
    private String detail;
    private URI instance;
    private LocalDateTime timestamp;
    private Map<String, Object> extensions;
    
    protected ProblemDetail() {
        this.timestamp = LocalDateTime.now();
    }
    
    private ProblemDetail(URI type, String title, int status, String detail, URI instance) {
        this();
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
    }
    
    public static ProblemDetail of(HttpStatus status, String title, String detail) {
        return new ProblemDetail(
                URI.create("https://api.ecommerce.com/problems/" + status.value()),
                title,
                status.value(),
                detail,
                null
        );
    }
    
    public static ProblemDetail of(HttpStatus status, String title, String detail, String instance) {
        return new ProblemDetail(
                URI.create("https://api.ecommerce.com/problems/" + status.value()),
                title,
                status.value(),
                detail,
                URI.create(instance)
        );
    }
    
    public static ProblemDetail validationError(String detail) {
        return of(HttpStatus.BAD_REQUEST, "Validation Error", detail);
    }
    
    public static ProblemDetail notFound(String detail) {
        return of(HttpStatus.NOT_FOUND, "Resource Not Found", detail);
    }
    
    public static ProblemDetail conflict(String detail) {
        return of(HttpStatus.CONFLICT, "Conflict", detail);
    }
    
    public static ProblemDetail internalError(String detail) {
        return of(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", detail);
    }
    
    public ProblemDetail withExtension(String key, Object value) {
        if (this.extensions == null) {
            this.extensions = new java.util.HashMap<>();
        }
        this.extensions.put(key, value);
        return this;
    }
    
    public URI getType() {
        return type;
    }
    
    public String getTitle() {
        return title;
    }
    
    public int getStatus() {
        return status;
    }
    
    public String getDetail() {
        return detail;
    }
    
    public URI getInstance() {
        return instance;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public Map<String, Object> getExtensions() {
        return extensions;
    }
}




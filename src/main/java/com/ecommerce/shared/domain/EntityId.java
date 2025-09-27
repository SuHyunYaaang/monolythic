package com.ecommerce.shared.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class EntityId {
    
    @NotNull
    private String value;
    
    protected EntityId() {
        // JPA
    }
    
    private EntityId(String value) {
        this.value = value;
    }
    
    public static EntityId generate() {
        return new EntityId(UUID.randomUUID().toString());
    }
    
    public static EntityId of(String value) {
        Objects.requireNonNull(value, "EntityId value cannot be null");
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException("EntityId value cannot be empty");
        }
        return new EntityId(value);
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityId entityId = (EntityId) o;
        return Objects.equals(value, entityId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}


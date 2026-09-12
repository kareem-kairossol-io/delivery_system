package io.kairos.delivery_system.core.database;

import java.time.Instant;
import java.util.Objects;

public abstract class BaseEntity<ID> {
    private ID id;
    private Instant createdAt;
    private Instant updatedAt;

    // Default constructor for entity instantiation
    protected BaseEntity() {}

    // Constructor for rehydration from repository or new creation
    protected BaseEntity(ID id, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public ID getId() { return id; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    // Setters (Protected so child entities or repositories in core can assign)
    public void setId(ID id) { this.id = id; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // Helper method to refresh timestamp before updating
    public void touch() {
        this.updatedAt = Instant.now();
    }

    // Entity Identity: Two entities of the same class are equal if their non-null IDs match
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity<?> that = (BaseEntity<?>) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : super.hashCode();
    }
}
package com.tapioca.BE.adapter.out.entity.project;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BaseEntity {

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    protected LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    protected LocalDateTime updatedAt;

    @Column(name = "deleted_at", nullable = true)
    protected LocalDateTime deletedAt;

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void restore() { this.deletedAt = null; }
}


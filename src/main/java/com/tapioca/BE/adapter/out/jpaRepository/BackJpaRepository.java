package com.tapioca.BE.adapter.out.jpaRepository;

import com.tapioca.BE.adapter.out.entity.BackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BackJpaRepository extends JpaRepository<BackEntity, UUID> {
    public BackEntity save(BackEntity backEntity);
}

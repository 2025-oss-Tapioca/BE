package com.tapioca.BE.adapter.out.jpaRepository;

import com.tapioca.BE.adapter.out.entity.FrontEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FrontJpaRepository extends JpaRepository<FrontEntity, UUID> {
    public FrontEntity save(FrontEntity frontEntity);
}

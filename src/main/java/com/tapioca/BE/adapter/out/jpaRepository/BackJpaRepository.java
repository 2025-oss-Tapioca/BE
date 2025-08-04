package com.tapioca.BE.adapter.out.jpaRepository;

import com.tapioca.BE.adapter.out.entity.BackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BackJpaRepository extends JpaRepository<BackEntity, UUID> {
    public BackEntity findByTeamEntity_id(UUID teamId);
    public BackEntity save(BackEntity backEntity);
}

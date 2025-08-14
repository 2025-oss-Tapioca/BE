package com.tapioca.BE.adapter.out.jpaRepository;

import com.tapioca.BE.adapter.out.entity.project.FrontEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FrontJpaRepository extends JpaRepository<FrontEntity, UUID> {
    public FrontEntity save(FrontEntity frontEntity);
    public FrontEntity findByTeamEntity_code(String teamCode);
    public boolean existsByTeamEntity_code(String teamCode);
}

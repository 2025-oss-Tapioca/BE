package com.tapioca.BE.adapter.out.jpaRepository;

import com.tapioca.BE.adapter.out.entity.project.FrontEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FrontJpaRepository extends JpaRepository<FrontEntity, UUID> {
    public FrontEntity save(FrontEntity frontEntity);
    public Optional<FrontEntity> findByTeamEntity_CodeAndDeletedAtIsNull(String teamCode);
    public boolean existsByTeamEntity_CodeAndDeletedAtIsNull(String teamCode);
}

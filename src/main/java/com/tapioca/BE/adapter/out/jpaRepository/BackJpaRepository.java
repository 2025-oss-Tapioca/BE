package com.tapioca.BE.adapter.out.jpaRepository;

import com.tapioca.BE.adapter.out.entity.project.BackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BackJpaRepository extends JpaRepository<BackEntity, UUID> {
    public BackEntity findByTeamEntity_IdAndDeletedAtIsNull(UUID teamId);
    public Optional<BackEntity> findByTeamEntity_CodeAndDeletedAtIsNull(String teamCode);
    public BackEntity save(BackEntity backEntity);
    public boolean existsByTeamEntity_CodeAndDeletedAtIsNull(String teamCode);
    public void delete(BackEntity backEntity);
    public boolean existsByTeamEntity_CodeAndDeletedAtIsNotNull(String teamCode);
    public Optional<BackEntity> findByTeamEntity_CodeAndDeletedAtIsNotNull(String teamCode);
}

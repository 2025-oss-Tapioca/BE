package com.tapioca.BE.adapter.out.jpaRepository;

import com.tapioca.BE.adapter.out.entity.project.DbEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DbJpaRepository extends JpaRepository<DbEntity, UUID> {
    public DbEntity save(DbEntity dbEntity);
    public Optional<DbEntity> findByTeamEntity_CodeAndDeletedAtIsNull(String teamCode);
    public boolean existsByTeamEntity_CodeAndDeletedAtIsNull(String teamCode);
}

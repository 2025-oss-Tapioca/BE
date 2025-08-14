package com.tapioca.BE.adapter.out.jpaRepository;

import com.tapioca.BE.adapter.out.entity.project.BackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BackJpaRepository extends JpaRepository<BackEntity, UUID> {
    public BackEntity findByTeamEntity_Id(UUID teamId);
    public BackEntity findByTeamEntity_code(String teamCode);
    public BackEntity save(BackEntity backEntity);
    public boolean existsByTeamEntity_code(String teamCode);
}

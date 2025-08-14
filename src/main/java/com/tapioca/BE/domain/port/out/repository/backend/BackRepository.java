package com.tapioca.BE.domain.port.out.repository.backend;

import com.tapioca.BE.adapter.out.entity.project.BackEntity;

import java.util.Optional;
import java.util.UUID;

public interface BackRepository {
    public BackEntity findByTeamEntity_Id(UUID teamId);
    public Optional<BackEntity> findByTeamCode(String teamCode);
    public BackEntity save(BackEntity backEntity);
    public boolean existsByTeamCode(String teamCode);
    public void delete(BackEntity backEntity);
    public boolean isSoftDeleted(String teamCode);
    public Optional<BackEntity> findByTeamEntity_CodeAndDeletedAtIsNotNull(String teamCode);
}

package com.tapioca.BE.domain.port.out.repository.backend;

import com.tapioca.BE.adapter.out.entity.project.BackEntity;

import java.util.UUID;

public interface BackRepository {
    public BackEntity findByTeamEntity_Id(UUID teamId);
    public BackEntity save(BackEntity backEntity);
}

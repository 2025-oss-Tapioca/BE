package com.tapioca.BE.domain.port.out.repository.db;

import com.tapioca.BE.adapter.out.entity.project.DbEntity;

import java.util.Optional;

public interface DbRepository {
    public DbEntity save(DbEntity dbEntity);
    public Optional<DbEntity> findByTeamCode(String teamCode);
    public boolean existsByTeamCode(String teamCode);
}

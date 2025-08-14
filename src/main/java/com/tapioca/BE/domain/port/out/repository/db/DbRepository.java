package com.tapioca.BE.domain.port.out.repository.db;

import com.tapioca.BE.adapter.out.entity.project.DbEntity;

public interface DbRepository {
    public DbEntity save(DbEntity dbEntity);
    public DbEntity findByTeamCode(String teamCode);
    public boolean existsByTeamCode(String teamCode);
}

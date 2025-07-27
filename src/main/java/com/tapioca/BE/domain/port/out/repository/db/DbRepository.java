package com.tapioca.BE.domain.port.out.repository.db;

import com.tapioca.BE.adapter.out.entity.DbEntity;

public interface DbRepository {
    public void save(DbEntity dbEntity);
}

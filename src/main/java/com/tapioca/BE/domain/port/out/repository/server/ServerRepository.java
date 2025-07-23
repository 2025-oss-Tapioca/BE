package com.tapioca.BE.domain.port.out.repository.server;

import com.tapioca.BE.adapter.out.entity.BackEntity;
import com.tapioca.BE.adapter.out.entity.ServerEntity;

public interface ServerRepository {
    // public void save(ServerEntity serverEntity);
    public void save(BackEntity backEntity);
}

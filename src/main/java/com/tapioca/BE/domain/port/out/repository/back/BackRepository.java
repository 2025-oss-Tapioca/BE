package com.tapioca.BE.domain.port.out.repository.back;

import com.tapioca.BE.adapter.out.entity.BackEntity;

public interface BackRepository {
    public void save(BackEntity backEntity);
}

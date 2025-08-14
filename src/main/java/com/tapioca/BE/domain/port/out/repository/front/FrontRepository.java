package com.tapioca.BE.domain.port.out.repository.front;

import com.tapioca.BE.adapter.out.entity.project.FrontEntity;

import java.util.Optional;

public interface FrontRepository {
    public FrontEntity save(FrontEntity frontEntity);
    public Optional<FrontEntity> findByCode(String teamCode);
    public boolean existsByCode(String teamCode);
}

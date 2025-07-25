package com.tapioca.BE.domain.port.out.repository.erd;

import com.tapioca.BE.domain.model.Erd;

import java.util.Optional;
import java.util.UUID;

public interface ErdRepository {
    Optional<Erd> findByTeamId(UUID teamId);
    Erd save(Erd erd);
}

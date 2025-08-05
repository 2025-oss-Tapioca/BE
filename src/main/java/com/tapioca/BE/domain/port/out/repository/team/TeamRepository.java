package com.tapioca.BE.domain.port.out.repository.team;

import com.tapioca.BE.adapter.out.entity.TeamEntity;

import java.util.UUID;

public interface TeamRepository {
    TeamEntity save(TeamEntity team);
    void deleteById(UUID teamId);
    TeamEntity findByTeamId(UUID teamId);
}

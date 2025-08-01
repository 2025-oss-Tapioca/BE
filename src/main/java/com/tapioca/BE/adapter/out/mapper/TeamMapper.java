package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.adapter.out.entity.TeamEntity;
import com.tapioca.BE.domain.model.Team;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {
    public static Team toDomain(TeamEntity teamEntity) {
        return new Team(
                teamEntity.getId(),
                teamEntity.getName(),
                teamEntity.getCode()
        );
    }

    public static TeamEntity toEntity(Team team) {
        return new TeamEntity(
                team.getId(),
                team.getName(),
                team.getCode()
        );
    }
}

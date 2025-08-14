package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.adapter.out.entity.user.TeamEntity;
import com.tapioca.BE.application.dto.request.team.CreateTeamRequestDto;
import com.tapioca.BE.domain.model.user.Team;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {
    public static Team toDomain(CreateTeamRequestDto dto) {
        return new Team(
                dto.teamName(),
                dto.teamDescription()
        );
    }
}

package com.tapioca.BE.domain.port.in.usecase.team;

import com.tapioca.BE.application.dto.request.team.CreateTeamRequestDto;
import com.tapioca.BE.application.dto.response.team.TeamResponseDto;

import java.util.UUID;

public interface TeamUseCase {
    TeamResponseDto getTeamInfo(UUID userId);
    TeamResponseDto createTeam(UUID userId, CreateTeamRequestDto createTeamRequestDto);
    TeamResponseDto joinTeam(UUID userId, String teamCode);
    void leaveTeam(UUID userId);
}

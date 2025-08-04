package com.tapioca.BE.domain.port.in.usecase.team;

import com.tapioca.BE.application.dto.request.team.CreateTeamRequestDto;
import com.tapioca.BE.application.dto.response.team.TeamResponseDto;
import com.tapioca.BE.application.dto.response.team.TeamsDto;

import java.util.List;
import java.util.UUID;

public interface TeamUseCase {
    List<TeamsDto> getTeam(UUID userId);
    TeamResponseDto getTeamInfo(UUID userId, String teamCode);
    TeamResponseDto createTeam(UUID userId, CreateTeamRequestDto createTeamRequestDto);
    TeamResponseDto joinTeam(UUID userId, String teamCode);
    void leaveTeam(UUID userId, String teamCode);
}

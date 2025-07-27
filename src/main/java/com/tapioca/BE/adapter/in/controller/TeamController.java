package com.tapioca.BE.adapter.in.controller;

import com.tapioca.BE.adapter.out.entity.UserEntity;
import com.tapioca.BE.application.dto.request.team.CreateTeamRequestDto;
import com.tapioca.BE.application.dto.request.team.JoinTeamRequestDto;
import com.tapioca.BE.application.dto.response.team.TeamResponseDto;
import com.tapioca.BE.config.common.CommonResponseDto;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.config.security.CustomUserDetails;
import com.tapioca.BE.domain.port.in.usecase.team.TeamUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TeamController {
    private final TeamUseCase teamUseCase;

    @GetMapping("/api/team")
    public CommonResponseDto<?> getTeamInfo(@AuthenticationPrincipal CustomUserDetails user) {
        return CommonResponseDto.ok(teamUseCase.getTeamInfo(user.getUserId()));
    }

    @PostMapping("/api/team/create")
    public CommonResponseDto<?> createTeam(@AuthenticationPrincipal CustomUserDetails user, @RequestBody CreateTeamRequestDto requestDto) {
        return CommonResponseDto.ok(teamUseCase.createTeam(user.getUserId(), requestDto));
    }

    @PostMapping("/api/team/join")
    public CommonResponseDto<?> joinTeam(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody JoinTeamRequestDto request
    ) {
        TeamResponseDto response = teamUseCase.joinTeam(user.getUserId(), request.teamCode());
        if (response == null) {
            return CommonResponseDto.fail(new CustomException(ErrorCode.NOT_FOUND_TEAM));
        }
        return CommonResponseDto.ok(response);
    }

    @DeleteMapping("/api/team/leave")
    public CommonResponseDto<?> leaveTeam(@AuthenticationPrincipal CustomUserDetails user) {
        teamUseCase.leaveTeam(user.getUserId());
        return CommonResponseDto.ok(null);
    }
}

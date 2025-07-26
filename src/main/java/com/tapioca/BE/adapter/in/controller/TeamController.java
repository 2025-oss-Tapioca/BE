package com.tapioca.BE.adapter.in.controller;

import com.tapioca.BE.adapter.out.entity.UserEntity;
import com.tapioca.BE.application.dto.request.team.CreateTeamRequestDto;
import com.tapioca.BE.config.common.CommonResponseDto;
import com.tapioca.BE.config.security.CustomUserDetails;
import com.tapioca.BE.domain.port.in.usecase.team.TeamUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}

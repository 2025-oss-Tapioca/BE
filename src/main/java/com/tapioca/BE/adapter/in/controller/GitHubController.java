package com.tapioca.BE.adapter.in.controller;

import com.tapioca.BE.application.dto.request.common.ReadServerRequestDto;
import com.tapioca.BE.application.dto.request.team.GitHubRequestDto;
import com.tapioca.BE.config.common.CommonResponseDto;
import com.tapioca.BE.config.security.CustomUserDetails;
import com.tapioca.BE.domain.port.in.usecase.github.GitHubDeleteUseCase;
import com.tapioca.BE.domain.port.in.usecase.github.GitHubReadUseCase;
import com.tapioca.BE.domain.port.in.usecase.github.GitHubRegisterUseCase;
import com.tapioca.BE.domain.port.in.usecase.github.GitHubUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/github")
public class GitHubController {

    private final GitHubRegisterUseCase gitHubRegisterUseCase;
    private final GitHubDeleteUseCase gitHubDeleteUseCase;
    private final GitHubUpdateUseCase gitHubUpdateUseCase;
    private final GitHubReadUseCase gitHubReadUseCase;

    @PostMapping
    public CommonResponseDto<?> registerGithub(
            @RequestBody GitHubRequestDto gitHubRequestDto
    ) {
        gitHubRegisterUseCase.registerGitHub(gitHubRequestDto);
        return CommonResponseDto.ok(null);
    }

    @DeleteMapping
    public CommonResponseDto<?> deleteGithub(
            @RequestBody ReadServerRequestDto readServerRequestDto
    ) {
        gitHubDeleteUseCase.delete(readServerRequestDto);
        return CommonResponseDto.noContent();
    }

    @PatchMapping
    public CommonResponseDto<?> updateGitHub(
            @RequestBody GitHubRequestDto gitHubRequestDto
    ) {
        return CommonResponseDto.ok(gitHubUpdateUseCase.update(gitHubRequestDto));
    }

    @GetMapping("/{teamCode}")
    public CommonResponseDto<?> readGithub(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable String teamCode
    ) {
        return CommonResponseDto.ok(gitHubReadUseCase.read(user.getUserId(), teamCode));
    }
}

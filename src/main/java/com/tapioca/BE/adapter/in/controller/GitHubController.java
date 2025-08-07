package com.tapioca.BE.adapter.in.controller;

import com.tapioca.BE.application.dto.request.team.GitHubRequestDto;
import com.tapioca.BE.config.common.CommonResponseDto;
import com.tapioca.BE.domain.port.in.usecase.github.GitHubUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/github")
public class GitHubController {

    private final GitHubUseCase gitHubUseCase;

    @PostMapping
    public CommonResponseDto<?> registerGithub(
            @RequestBody GitHubRequestDto gitHubRequestDto
    ) {
        return CommonResponseDto.ok(gitHubUseCase.registerGitHub(gitHubRequestDto));
    }

    @PatchMapping
    public CommonResponseDto<?> updateGitHub(
            @RequestBody GitHubRequestDto gitHubRequestDto
    ) {
        return CommonResponseDto.ok(gitHubUseCase.updateGitHub(gitHubRequestDto));
    }
}

package com.tapioca.BE.adapter.in.controller;

import com.tapioca.BE.application.dto.request.team.GitHubRequestDto;
import com.tapioca.BE.config.common.CommonResponseDto;
import com.tapioca.BE.domain.port.in.usecase.github.GitHubUseCase;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/github")
public class GitHubController {

    private final GitHubUseCase gitHubUseCase;

    @PostMapping("/{teamCode}")
    public CommonResponseDto<?> registerGithub(
            @RequestBody GitHubRequestDto gitHubRequestDto,
            @PathVariable String teamCode
    ) {
        return CommonResponseDto.ok(gitHubUseCase.registerGitHub(gitHubRequestDto, teamCode));
    }

    @PatchMapping("/{teamCode}")
    public CommonResponseDto<?> updateGitHub(
            @RequestBody GitHubRequestDto gitHubRequestDto,
            @PathVariable String teamCode
    ) {
        return CommonResponseDto.ok(gitHubUseCase.updateGitHub(gitHubRequestDto, teamCode));
    }
}

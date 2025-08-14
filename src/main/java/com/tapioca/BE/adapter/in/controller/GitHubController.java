package com.tapioca.BE.adapter.in.controller;

import com.tapioca.BE.application.dto.request.common.DeleteServerRequestDto;
import com.tapioca.BE.application.dto.request.team.GitHubRequestDto;
import com.tapioca.BE.config.common.CommonResponseDto;
import com.tapioca.BE.domain.port.in.usecase.github.GitHubDeleteUseCase;
import com.tapioca.BE.domain.port.in.usecase.github.GitHubRegisterUseCase;
import com.tapioca.BE.domain.port.in.usecase.github.GitHubUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/github")
public class GitHubController {

    private final GitHubRegisterUseCase gitHubRegisterUseCase;
    private final GitHubDeleteUseCase gitHubDeleteUseCase;
    private final GitHubUpdateUseCase gitHubUpdateUseCase;

    @PostMapping
    public CommonResponseDto<?> registerGithub(
            @RequestBody GitHubRequestDto gitHubRequestDto
    ) {
        gitHubRegisterUseCase.registerGitHub(gitHubRequestDto);
        return CommonResponseDto.ok(null);
    }

    @DeleteMapping
    public CommonResponseDto<?> deleteGithub(
            @RequestBody DeleteServerRequestDto deleteServerRequestDto
    ) {
        gitHubDeleteUseCase.delete(deleteServerRequestDto);
        return CommonResponseDto.noContent();
    }

    @PatchMapping
    public CommonResponseDto<?> updateGitHub(
            @RequestBody GitHubRequestDto gitHubRequestDto
    ) {
        return CommonResponseDto.ok(gitHubUpdateUseCase.update(gitHubRequestDto));
    }
}

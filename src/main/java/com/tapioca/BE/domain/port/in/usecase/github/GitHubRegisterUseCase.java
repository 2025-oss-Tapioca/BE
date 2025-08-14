package com.tapioca.BE.domain.port.in.usecase.github;

import com.tapioca.BE.application.dto.request.team.GitHubRequestDto;
import com.tapioca.BE.application.dto.response.team.GitHubResponseDto;

public interface GitHubRegisterUseCase {
    GitHubResponseDto registerGitHub(GitHubRequestDto gitHubRequestDto);
}
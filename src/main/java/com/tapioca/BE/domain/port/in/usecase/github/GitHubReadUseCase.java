package com.tapioca.BE.domain.port.in.usecase.github;

import com.tapioca.BE.application.dto.request.common.ReadServerRequestDto;
import com.tapioca.BE.application.dto.request.team.GitHubRequestDto;
import com.tapioca.BE.application.dto.response.team.GitHubResponseDto;

import java.util.List;
import java.util.UUID;

public interface GitHubReadUseCase {
    public GitHubResponseDto read(UUID userId, String teamCode);
}

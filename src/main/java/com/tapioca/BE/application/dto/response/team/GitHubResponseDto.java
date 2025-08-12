package com.tapioca.BE.application.dto.response.team;

public record GitHubResponseDto(
        String teamCode,
        String repoUrl,
        boolean isPrivate,
        String accessToken,
        String defaultBranch
) {}

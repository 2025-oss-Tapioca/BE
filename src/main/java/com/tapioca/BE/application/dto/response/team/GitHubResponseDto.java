package com.tapioca.BE.application.dto.response.team;

public record GitHubResponseDto(
        String repoUrl,
        boolean isPrivate,
        String accessToken,
        String defaultBranch
) {}

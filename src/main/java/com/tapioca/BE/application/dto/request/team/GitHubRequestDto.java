package com.tapioca.BE.application.dto.request.team;

public record GitHubRequestDto(
        String teamCode,
        String repoUrl,
        boolean isPrivate,
        String accessToken,
        String defaultBranch
) {}
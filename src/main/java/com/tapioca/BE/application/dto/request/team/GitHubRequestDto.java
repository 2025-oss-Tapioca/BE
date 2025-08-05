package com.tapioca.BE.application.dto.request.team;

public record GitHubRequestDto(
        String repoUrl,
        boolean isPrivate,
        String accessToken,
        String defaultBranch
) {}
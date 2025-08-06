package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.adapter.out.entity.project.GitHubEntity;
import com.tapioca.BE.adapter.out.entity.user.TeamEntity;
import com.tapioca.BE.application.dto.request.team.GitHubRequestDto;
import com.tapioca.BE.domain.model.project.GitHub;
import org.springframework.stereotype.Component;

@Component
public class GithubMapper {
    public GitHub toDomain(GitHubRequestDto dto) {
        return new GitHub(
                dto.repoUrl(),
                dto.isPrivate(),
                dto.accessToken(),
                dto.defaultBranch()
        );
    }

    public GitHub toDomain(GitHubEntity gitHubEntity) {
        return new GitHub(
                gitHubEntity.getRepoUrl(),
                gitHubEntity.isPrivate(),
                gitHubEntity.getAccessToken(),
                gitHubEntity.getDefaultBranch()
        );
    }

    public GitHubEntity toEntity(GitHub github, TeamEntity teamEntity) {
        return GitHubEntity.builder()
                .teamEntity(teamEntity)
                .repoUrl(github.getRepoUrl())
                .isPrivate(github.isPrivate())
                .accessToken(github.getAccessToken())
                .defaultBranch(github.getDefaultBranch())
                .build();
    }

    // github update
    public GitHubEntity toEntity(GitHub updated, GitHubEntity existing, TeamEntity teamEntity) {
        return GitHubEntity.builder()
                .id(existing.getId())
                .teamEntity(teamEntity)
                .repoUrl(updated.getRepoUrl())
                .isPrivate(updated.isPrivate())
                .accessToken(updated.getAccessToken())
                .defaultBranch(updated.getDefaultBranch())
                .build();
    }
}

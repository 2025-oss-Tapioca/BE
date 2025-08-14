package com.tapioca.BE.domain.port.out.repository.github;

import com.tapioca.BE.adapter.out.entity.project.GitHubEntity;

import java.util.Optional;

public interface GithubRepository {
    public GitHubEntity save(GitHubEntity gitHubEntity);
    public Optional<GitHubEntity> findByTeamCode(String teamCode);
    public boolean existsByTeamCode(String teamCode);
    public boolean isSoftDeleted(String teamCode);
    public Optional<GitHubEntity> findByTeamEntity_CodeAndDeletedAtIsNotNull(String teamCode);
}

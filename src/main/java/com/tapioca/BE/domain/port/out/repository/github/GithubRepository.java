package com.tapioca.BE.domain.port.out.repository.github;

import com.tapioca.BE.adapter.out.entity.project.GitHubEntity;

public interface GithubRepository {
    public GitHubEntity save(GitHubEntity gitHubEntity);
    public GitHubEntity findByTeamCode(String teamCode);
}

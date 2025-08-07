package com.tapioca.BE.adapter.out.jpaRepository;

import com.tapioca.BE.adapter.out.entity.project.GitHubEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GithubJpaRepository extends JpaRepository<GitHubEntity, UUID> {
    public GitHubEntity save(GitHubEntity gitHubEntity);
    Optional<GitHubEntity> findByTeamEntity_Code(String code);
}

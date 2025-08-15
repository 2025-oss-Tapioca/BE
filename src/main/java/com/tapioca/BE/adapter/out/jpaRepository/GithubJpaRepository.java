package com.tapioca.BE.adapter.out.jpaRepository;

import com.tapioca.BE.adapter.out.entity.project.GitHubEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GithubJpaRepository extends JpaRepository<GitHubEntity, UUID> {
    public GitHubEntity save(GitHubEntity gitHubEntity);
    public Optional<GitHubEntity> findByTeamEntity_CodeAndDeletedAtIsNull(String code);
    public boolean existsByTeamEntity_CodeAndDeletedAtIsNull(String code);
    public boolean existsByTeamEntity_CodeAndDeletedAtIsNotNull(String code);
    public Optional<GitHubEntity> findByTeamEntity_CodeAndDeletedAtIsNotNull(String code);
    public List<GitHubEntity> findAllByTeamEntity_CodeAndDeletedAtIsNull(String code);
}

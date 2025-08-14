package com.tapioca.BE.adapter.out.repositoryImpl;

import com.tapioca.BE.adapter.out.entity.project.GitHubEntity;
import com.tapioca.BE.adapter.out.jpaRepository.GithubJpaRepository;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.port.out.repository.github.GithubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GithubRepositoryImpl implements GithubRepository {

    private final GithubJpaRepository githubJpaRepository;

    @Override
    public GitHubEntity save(GitHubEntity gitHubEntity) {
        return githubJpaRepository.save(gitHubEntity);
    }

    @Override
    public Optional<GitHubEntity> findByTeamCode(String teamCode) {
        return githubJpaRepository.findByTeamEntity_CodeAndDeletedAtIsNull(teamCode);
    }

    @Override
    public boolean existsByTeamCode(String teamCode) {
        return githubJpaRepository.existsByTeamEntity_CodeAndDeletedAtIsNull(teamCode);
    }

    @Override
    public boolean isSoftDeleted(String teamCode) {
        return githubJpaRepository.existsByTeamEntity_CodeAndDeletedAtIsNotNull(teamCode);
    }

    @Override
    public Optional<GitHubEntity> findByTeamEntity_CodeAndDeletedAtIsNotNull(String teamCode) {
        return githubJpaRepository.findByTeamEntity_CodeAndDeletedAtIsNotNull(teamCode);
    }
}

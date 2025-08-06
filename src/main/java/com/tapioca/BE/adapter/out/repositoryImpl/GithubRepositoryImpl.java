package com.tapioca.BE.adapter.out.repositoryImpl;

import com.tapioca.BE.adapter.out.entity.project.GitHubEntity;
import com.tapioca.BE.adapter.out.jpaRepository.GithubJpaRepository;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.port.out.repository.github.GithubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GithubRepositoryImpl implements GithubRepository {

    private final GithubJpaRepository githubJpaRepository;

    @Override
    public GitHubEntity save(GitHubEntity gitHubEntity) { return githubJpaRepository.save(gitHubEntity); }

    @Override
    public GitHubEntity findByTeamCode(String teamCode) {
        return githubJpaRepository.findByTeamEntity_Code(teamCode)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEAM));
    }
}

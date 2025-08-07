package com.tapioca.BE.application.service.github;

import com.tapioca.BE.adapter.out.entity.project.GitHubEntity;
import com.tapioca.BE.adapter.out.entity.user.TeamEntity;
import com.tapioca.BE.adapter.out.mapper.GithubMapper;
import com.tapioca.BE.application.dto.request.team.GitHubRequestDto;
import com.tapioca.BE.application.dto.response.team.GitHubResponseDto;
import com.tapioca.BE.domain.model.project.GitHub;
import com.tapioca.BE.domain.port.in.usecase.github.GitHubUseCase;
import com.tapioca.BE.domain.port.out.repository.github.GithubRepository;
import com.tapioca.BE.domain.port.out.repository.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GithubRegisterService implements GitHubUseCase {

    private final GithubRepository githubRepository;
    private final GithubMapper githubMapper;
    private final TeamRepository teamRepository;

    @Override
    public GitHubResponseDto registerGitHub(GitHubRequestDto gitHubRequestDto) {

        GitHub github = githubMapper.toDomain(gitHubRequestDto);

        TeamEntity teamEntity = teamRepository.findByTeamCode(github.getTeamCode());

        GitHubEntity savedEntity = githubMapper.toEntity(github, teamEntity);
        githubRepository.save(savedEntity);

        return new GitHubResponseDto(
                savedEntity.getTeamEntity().getCode(),
                savedEntity.getRepoUrl(),
                savedEntity.isPrivate(),
                savedEntity.getAccessToken(),
                savedEntity.getDefaultBranch()
        );
    }

    @Override
    public GitHubResponseDto updateGitHub(GitHubRequestDto gitHubRequestDto) {

        // 수정된 내용
        GitHub updated = githubMapper.toDomain(gitHubRequestDto);

        TeamEntity teamEntity = teamRepository.findByTeamCode(updated.getTeamCode());

        // 수정할 내용
        GitHubEntity existingEntity = githubRepository.findByTeamCode(teamEntity.getCode());

        GitHubEntity savedEntity = githubMapper.toEntity(updated, existingEntity, teamEntity);
        githubRepository.save(savedEntity);

        return new GitHubResponseDto(
                savedEntity.getTeamEntity().getCode(),
                savedEntity.getRepoUrl(),
                savedEntity.isPrivate(),
                savedEntity.getAccessToken(),
                savedEntity.getDefaultBranch()
        );
    }
}

package com.tapioca.BE.application.service.github;

import com.tapioca.BE.adapter.out.entity.project.GitHubEntity;
import com.tapioca.BE.adapter.out.entity.user.TeamEntity;
import com.tapioca.BE.adapter.out.mapper.GithubMapper;
import com.tapioca.BE.application.dto.request.team.GitHubRequestDto;
import com.tapioca.BE.application.dto.response.team.GitHubResponseDto;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.model.project.GitHub;
import com.tapioca.BE.domain.port.in.usecase.github.GitHubUpdateUseCase;
import com.tapioca.BE.domain.port.out.repository.github.GithubRepository;
import com.tapioca.BE.domain.port.out.repository.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GitHubUpdateService implements GitHubUpdateUseCase {

    private final GithubRepository githubRepository;
    private final GithubMapper githubMapper;
    private final TeamRepository teamRepository;

    @Override
    public GitHubResponseDto update(GitHubRequestDto gitHubRequestDto) {

        // 수정된 내용
        GitHub updated = githubMapper.toDomain(gitHubRequestDto);

        // soft deleted 되지 않은 gitHubEntity 있으면 중복 처리
        if (githubRepository.isSoftDeleted(updated.getTeamCode())) {
            throw new CustomException(ErrorCode.NOT_FOUND_GITHUB);
        }

        // 수정할 내용
        GitHubEntity existingEntity = githubRepository.findByTeamCode(updated.getTeamCode())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_GITHUB));

        TeamEntity teamEntity = teamRepository.findByTeamCode(updated.getTeamCode());

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

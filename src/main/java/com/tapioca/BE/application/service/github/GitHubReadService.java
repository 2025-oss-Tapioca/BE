package com.tapioca.BE.application.service.github;

import com.tapioca.BE.adapter.out.entity.project.GitHubEntity;
import com.tapioca.BE.adapter.out.mapper.GithubMapper;
import com.tapioca.BE.application.dto.request.common.ReadServerRequestDto;
import com.tapioca.BE.application.dto.request.team.GitHubRequestDto;
import com.tapioca.BE.application.dto.response.team.GitHubResponseDto;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.model.project.GitHub;
import com.tapioca.BE.domain.port.in.usecase.github.GitHubReadUseCase;
import com.tapioca.BE.domain.port.out.repository.github.GithubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GitHubReadService implements GitHubReadUseCase {

    private final GithubRepository githubRepository;
    private final GithubMapper githubMapper;

    @Override
    public GitHubResponseDto read(String teamCode) {

        GitHubEntity gitHubEntity = githubRepository.findByTeamCode(teamCode)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_GITHUB));

        GitHub gitHub = githubMapper.toDomain(gitHubEntity);

        return new GitHubResponseDto(
                gitHub.getTeamCode(),
                gitHub.getRepoUrl(),
                gitHub.isPrivate(),
                gitHub.getAccessToken(),
                gitHub.getDefaultBranch()
        );
    }
}

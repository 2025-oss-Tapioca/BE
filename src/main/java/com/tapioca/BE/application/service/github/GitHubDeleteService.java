package com.tapioca.BE.application.service.github;

import com.tapioca.BE.adapter.out.entity.project.GitHubEntity;
import com.tapioca.BE.adapter.out.mapper.GithubMapper;
import com.tapioca.BE.application.dto.request.common.DeleteServerRequestDto;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.model.project.GitHub;
import com.tapioca.BE.domain.port.in.usecase.github.GitHubDeleteUseCase;
import com.tapioca.BE.domain.port.out.repository.github.GithubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GitHubDeleteService implements GitHubDeleteUseCase {

    private final GithubRepository githubRepository;
    private final GithubMapper githubMapper;

    @Override
    public void delete(DeleteServerRequestDto deleteServerRequestDto) {

        GitHub gitHub = githubMapper.toDomain(deleteServerRequestDto);

        GitHubEntity gitHubEntity = githubRepository.findByTeamCode(gitHub.getTeamCode())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_GITHUB));

        // soft delete
        gitHubEntity.delete();
    }
}

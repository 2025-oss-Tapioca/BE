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
import com.tapioca.BE.domain.port.out.repository.team.TeamRepository;
import com.tapioca.BE.domain.port.out.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class GitHubReadService implements GitHubReadUseCase {

    private final GithubRepository githubRepository;
    private final GithubMapper githubMapper;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    @Override
    public GitHubResponseDto read(UUID userId, String teamCode) {

        // 존재하는 팀인지 확인
        teamRepository.findByTeamCode(teamCode);

        // user가 해당 팀의 멤버인지 확인
        if (!memberRepository.existsByUserIdAndTeamCode(userId, teamCode)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED_MEMBER);
        }

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

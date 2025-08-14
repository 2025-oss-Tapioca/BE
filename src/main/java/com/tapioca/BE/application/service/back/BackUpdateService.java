package com.tapioca.BE.application.service.back;

import com.tapioca.BE.adapter.out.entity.project.BackEntity;
import com.tapioca.BE.adapter.out.entity.user.TeamEntity;
import com.tapioca.BE.adapter.out.mapper.BackEndMapper;
import com.tapioca.BE.application.dto.request.back.RegisterRequestDto;
import com.tapioca.BE.application.dto.response.back.RegisterResponseDto;
import com.tapioca.BE.domain.model.project.BackEnd;
import com.tapioca.BE.domain.port.in.usecase.back.BackUpdateUseCase;
import com.tapioca.BE.domain.port.out.repository.backend.BackRepository;
import com.tapioca.BE.domain.port.out.repository.github.GithubRepository;
import com.tapioca.BE.domain.port.out.repository.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BackUpdateService implements BackUpdateUseCase {

    private final BackRepository backRepository;
    private final BackEndMapper backEndMapper;
    private final TeamRepository teamRepository;

    @Override
    public RegisterResponseDto update(RegisterRequestDto updateRequestDto) {

        // 수정한 내용
        BackEnd updated = backEndMapper.toDomain(updateRequestDto);

        TeamEntity teamEntity = teamRepository.findByTeamCode(updated.getTeamCode());

        // 수정 대상
        BackEntity existingEntity = backRepository.findByTeamCode(teamEntity.getCode());

        BackEntity savedEntity = backEndMapper.toEntity(updated, existingEntity, teamEntity);
        backRepository.save(savedEntity);

        return new RegisterResponseDto(
                savedEntity.getTeamEntity().getCode(),
                savedEntity.getLoginPath(),
                savedEntity.getEc2Url(),
                savedEntity.getAuthToken(),
                savedEntity.getOs(),
                savedEntity.getEnv()
        );
    }
}

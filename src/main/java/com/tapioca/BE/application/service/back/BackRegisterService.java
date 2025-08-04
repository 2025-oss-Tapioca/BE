package com.tapioca.BE.application.service.back;

import com.tapioca.BE.adapter.out.entity.BackEntity;
import com.tapioca.BE.adapter.out.entity.MemberEntity;
import com.tapioca.BE.adapter.out.entity.TeamEntity;
import com.tapioca.BE.adapter.out.mapper.BackEndMapper;
import com.tapioca.BE.application.dto.request.back.RegisterRequestDto;
import com.tapioca.BE.application.dto.response.back.RegisterResponseDto;
import com.tapioca.BE.domain.model.BackEnd;
import com.tapioca.BE.domain.port.in.usecase.back.BackRegisterUseCase;
import com.tapioca.BE.domain.port.out.repository.backend.BackRepository;
import com.tapioca.BE.domain.port.out.repository.team.TeamRepository;
import com.tapioca.BE.domain.port.out.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class BackRegisterService implements BackRegisterUseCase {

    private final BackRepository backRepository;
    private final BackEndMapper backMapper;
    private final TeamRepository teamRepository;

    @Override
    public RegisterResponseDto register(RegisterRequestDto dto) {

        TeamEntity teamEntity = teamRepository.findByTeamId(dto.teamId());

        BackEnd backend = backMapper.toDomain(dto);

        BackEntity savedEntity = backMapper.toEntity(backend, teamEntity);
        backRepository.save(savedEntity);

        return new RegisterResponseDto(
                savedEntity.getTeamEntity().getId(),
                savedEntity.getEc2Host(),
                savedEntity.getEc2Url(),
                savedEntity.getAuthToken(),
                savedEntity.getOs(),
                savedEntity.getEnv()
        );
    }
}

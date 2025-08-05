package com.tapioca.BE.application.service.back;

import com.tapioca.BE.adapter.out.entity.project.BackEntity;
import com.tapioca.BE.adapter.out.entity.user.TeamEntity;
import com.tapioca.BE.adapter.out.mapper.BackEndMapper;
import com.tapioca.BE.application.dto.request.back.RegisterRequestDto;
import com.tapioca.BE.application.dto.response.back.RegisterResponseDto;
import com.tapioca.BE.domain.model.project.BackEnd;
import com.tapioca.BE.domain.port.in.usecase.back.BackRegisterUseCase;
import com.tapioca.BE.domain.port.out.repository.backend.BackRepository;
import com.tapioca.BE.domain.port.out.repository.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BackRegisterService implements BackRegisterUseCase {

    private final BackRepository backRepository;
    private final BackEndMapper backMapper;
    private final TeamRepository teamRepository;

    @Override
    public RegisterResponseDto register(RegisterRequestDto dto, String teamCode) {

        TeamEntity teamEntity = teamRepository.findByTeamCode(teamCode);

        BackEnd backend = backMapper.toDomain(dto);

        BackEntity savedEntity = backMapper.toEntity(backend, teamEntity);
        backRepository.save(savedEntity);

        return new RegisterResponseDto(
                savedEntity.getLoginPath(),
                savedEntity.getEc2Url(),
                savedEntity.getOs(),
                savedEntity.getEnv()
        );
    }
}

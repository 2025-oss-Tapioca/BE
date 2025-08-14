package com.tapioca.BE.application.service.back;

import com.tapioca.BE.adapter.out.entity.project.BackEntity;
import com.tapioca.BE.adapter.out.entity.user.TeamEntity;
import com.tapioca.BE.adapter.out.mapper.BackEndMapper;
import com.tapioca.BE.application.dto.request.back.RegisterRequestDto;
import com.tapioca.BE.application.dto.response.back.RegisterResponseDto;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
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
    public RegisterResponseDto register(RegisterRequestDto dto) {

        BackEnd backend = backMapper.toDomain(dto);

        if (backRepository.existsByTeamCode(backend.getTeamCode())) {
            throw new CustomException(ErrorCode.CONFLICT_REGISTERED_BACK);
        }

        TeamEntity teamEntity = teamRepository.findByTeamCode(backend.getTeamCode());

        BackEntity savedEntity = backMapper.toEntity(backend, teamEntity);
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

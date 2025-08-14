package com.tapioca.BE.application.service.front;

import com.tapioca.BE.adapter.out.entity.project.FrontEntity;
import com.tapioca.BE.adapter.out.entity.user.TeamEntity;
import com.tapioca.BE.adapter.out.mapper.FrontMapper;
import com.tapioca.BE.application.dto.request.front.RegisterRequestDto;
import com.tapioca.BE.application.dto.response.front.RegisterResponseDto;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.model.project.Front;
import com.tapioca.BE.domain.port.in.usecase.front.FrontRegisterUseCase;
import com.tapioca.BE.domain.port.in.usecase.front.FrontUpdateUseCase;
import com.tapioca.BE.domain.port.out.repository.front.FrontRepository;
import com.tapioca.BE.domain.port.out.repository.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FrontUpdateService implements FrontUpdateUseCase {

    private final FrontRepository frontRepository;
    private final FrontMapper frontMapper;
    private final TeamRepository teamRepository;

    @Override
    public RegisterResponseDto update(RegisterRequestDto updateRequestDto) {

        // 수정한 내용
        Front updated = frontMapper.toDomain(updateRequestDto);

        // 수정할 대상
        FrontEntity existingEntity = frontRepository.findByCode(updated.getTeamCode())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FRONT));

        TeamEntity teamEntity = teamRepository.findByTeamCode(updated.getTeamCode());

        FrontEntity savedEntity = frontMapper.toEntity(updated, existingEntity, teamEntity);
        frontRepository.save(savedEntity);

        return new RegisterResponseDto(
                updated.getTeamCode(),
                updated.getEc2Host(),
                updated.getAuthToken(),
                updated.getEntryPoint(),
                updated.getOs(),
                updated.getEnv(),
                updated.getProtocol()
        );
    }
}

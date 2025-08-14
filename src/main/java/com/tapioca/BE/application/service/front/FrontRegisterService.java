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
import com.tapioca.BE.domain.port.out.repository.front.FrontRepository;
import com.tapioca.BE.domain.port.out.repository.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FrontRegisterService implements FrontRegisterUseCase {

    private final FrontRepository frontRepository;
    private final FrontMapper frontMapper;
    private final TeamRepository teamRepository;

    @Override
    public RegisterResponseDto register(RegisterRequestDto dto) {

        Front front = frontMapper.toDomain(dto);

        // 중복 등록 체크
        if (frontRepository.existsByCode(front.getTeamCode())) {
            throw new CustomException(ErrorCode.CONFLICT_REGISTERED_FRONT);
        }

        TeamEntity teamEntity = teamRepository.findByTeamCode(front.getTeamCode());

        FrontEntity frontEntity = frontMapper.toEntity(front, teamEntity);
        frontRepository.save(frontEntity);

        return new RegisterResponseDto(
                front.getTeamCode(),
                front.getEc2Host(),
                front.getAuthToken(),
                front.getEntryPoint(),
                front.getOs(),
                front.getEnv(),
                front.getProtocol()
        );
    }
}

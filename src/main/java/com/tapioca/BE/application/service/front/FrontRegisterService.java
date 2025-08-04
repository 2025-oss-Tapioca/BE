package com.tapioca.BE.application.service.front;

import com.tapioca.BE.adapter.out.entity.FrontEntity;
import com.tapioca.BE.adapter.out.entity.MemberEntity;
import com.tapioca.BE.adapter.out.entity.TeamEntity;
import com.tapioca.BE.adapter.out.mapper.FrontMapper;
import com.tapioca.BE.application.dto.request.front.RegisterRequestDto;
import com.tapioca.BE.application.dto.response.front.RegisterResponseDto;
import com.tapioca.BE.domain.model.Front;
import com.tapioca.BE.domain.port.in.usecase.front.FrontRegisterUseCase;
import com.tapioca.BE.domain.port.out.repository.front.FrontRepository;
import com.tapioca.BE.domain.port.out.repository.team.TeamRepository;
import com.tapioca.BE.domain.port.out.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class FrontRegisterService implements FrontRegisterUseCase {

    private final FrontRepository frontRepository;
    private final FrontMapper frontMapper;
    private final TeamRepository teamRepository;

    @Override
    public RegisterResponseDto register(RegisterRequestDto dto) {

        TeamEntity teamEntity = teamRepository.findByTeamId(dto.teamId());

        Front front = frontMapper.toDomain(dto);

        FrontEntity frontEntity = frontMapper.toEntity(front, teamEntity);
        frontRepository.save(frontEntity);

        return new RegisterResponseDto(
                frontEntity.getTeamEntity().getId(),
                frontEntity.getEc2Host(),
                frontEntity.getEntryPoint(),
                frontEntity.getOs(),
                frontEntity.getEnv(),
                frontEntity.getProtocol()
        );
    }
}

package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.adapter.out.entity.FrontEntity;
import com.tapioca.BE.adapter.out.entity.TeamEntity;
import com.tapioca.BE.application.dto.request.front.RegisterRequestDto;
import com.tapioca.BE.domain.model.Front;
import org.springframework.stereotype.Component;

@Component
public class FrontMapper {
    public Front toDomain(RegisterRequestDto registerRequestDto) {
        return new Front(
                null,
                registerRequestDto.teamId(),
                registerRequestDto.ec2Host(),
                registerRequestDto.entryPoint(),
                registerRequestDto.os(),
                registerRequestDto.env(),
                registerRequestDto.protocol()
        );
    }

    public FrontEntity toEntity(Front front, TeamEntity teamEntity) {
        return FrontEntity.builder()
                .id(front.getId())
                .teamEntity(teamEntity)
                .ec2Host(front.getEc2Host())
                .entryPoint(front.getEntryPoint())
                .os(front.getOs())
                .env(front.getEnv())
                .protocol(front.getProtocol())
                .build();
    }
}

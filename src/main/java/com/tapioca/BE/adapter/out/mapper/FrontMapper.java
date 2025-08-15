package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.adapter.out.entity.project.FrontEntity;
import com.tapioca.BE.adapter.out.entity.user.TeamEntity;
import com.tapioca.BE.application.dto.request.common.ReadServerRequestDto;
import com.tapioca.BE.application.dto.request.front.RegisterRequestDto;
import com.tapioca.BE.domain.model.project.Front;
import org.springframework.stereotype.Component;

@Component
public class FrontMapper {
    public Front toDomain(RegisterRequestDto registerRequestDto) {
        return new Front(
                registerRequestDto.teamCode(),
                registerRequestDto.ec2Host(),
                registerRequestDto.authToken(),
                registerRequestDto.entryPoint(),
                registerRequestDto.os(),
                registerRequestDto.env(),
                registerRequestDto.protocol()
        );
    }

    public Front toDomain(ReadServerRequestDto dto) {
        return new Front(
                dto.teamCode(),
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public Front toDomain(FrontEntity frontEntity) {
        return new Front(
                frontEntity.getTeamEntity().getCode(),
                frontEntity.getEc2Host(),
                frontEntity.getAuthToken(),
                frontEntity.getEntryPoint(),
                frontEntity.getOs(),
                frontEntity.getEnv(),
                frontEntity.getProtocol()
        );
    }

    public FrontEntity toEntity(Front front, TeamEntity teamEntity) {
        return FrontEntity.builder()
                .teamEntity(teamEntity)
                .ec2Host(front.getEc2Host())
                .authToken(front.getAuthToken())
                .entryPoint(front.getEntryPoint())
                .os(front.getOs())
                .env(front.getEnv())
                .protocol(front.getProtocol())
                .build();
    }

    // front update
    public FrontEntity toEntity(Front front, FrontEntity existing, TeamEntity teamEntity) {
        return FrontEntity.builder()
                .id(existing.getId())
                .teamEntity(teamEntity)
                .ec2Host(front.getEc2Host())
                .authToken(front.getAuthToken())
                .entryPoint(front.getEntryPoint())
                .os(front.getOs())
                .env(front.getEnv())
                .protocol(front.getProtocol())
                .build();
    }
}

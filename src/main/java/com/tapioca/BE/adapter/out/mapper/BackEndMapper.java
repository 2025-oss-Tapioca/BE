package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.adapter.out.entity.BackEntity;
import com.tapioca.BE.adapter.out.entity.TeamEntity;
import com.tapioca.BE.application.dto.request.back.RegisterRequestDto;
import com.tapioca.BE.domain.model.BackEnd;
import org.springframework.stereotype.Component;

@Component
public class BackEndMapper {
    public BackEnd toDomain(BackEntity backEntity){
        return new BackEnd(
                null,
                backEntity.getTeamEntity().getId(),
                backEntity.getEc2Host(),
                backEntity.getEc2Url(),
                backEntity.getAuthToken(),
                backEntity.getOs(),
                backEntity.getEnv()
                );
    }

    public BackEnd toDomain(RegisterRequestDto dto) {
        return new BackEnd(
                null,
                dto.teamId(),
                dto.ec2Host(),
                dto.ec2Url(),
                dto.authToken(),
                dto.os(),
                dto.env()
        );
    }

    public BackEntity toEntity(BackEnd backEnd, TeamEntity teamEntity) {
        return BackEntity.builder()
                .id(backEnd.getId())
                .teamEntity(teamEntity)
                .ec2Host(backEnd.getEc2Host())
                .authToken(backEnd.getAuthToken())
                .os(backEnd.getOs())
                .env(backEnd.getEnv())
                .build();
    }
}

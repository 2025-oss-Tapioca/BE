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
                backEntity.getLoginPath(),
                backEntity.getEc2Url(),
                backEntity.getAuthToken(),
                backEntity.getOs(),
                backEntity.getEnv()
                );
    }

    public BackEnd toDomain(RegisterRequestDto dto) {
        return new BackEnd(
                dto.loginPath(),
                dto.ec2Url(),
                dto.authToken(),
                dto.os(),
                dto.env()
        );
    }

    public BackEntity toEntity(BackEnd backEnd, TeamEntity teamEntity) {
        return BackEntity.builder()
                .teamEntity(teamEntity)
                .loginPath(backEnd.getLoginPath())
                .authToken(backEnd.getAuthToken())
                .os(backEnd.getOs())
                .env(backEnd.getEnv())
                .build();
    }
}

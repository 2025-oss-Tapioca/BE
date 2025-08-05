package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.adapter.out.entity.project.BackEntity;
import com.tapioca.BE.adapter.out.entity.user.TeamEntity;
import com.tapioca.BE.application.dto.request.back.RegisterRequestDto;
import com.tapioca.BE.domain.model.project.BackEnd;
import org.springframework.stereotype.Component;

@Component
public class BackEndMapper {
    public BackEnd toDomain(BackEntity backEntity){
        return new BackEnd(
                backEntity.getLoginPath(),
                backEntity.getOs(),
                backEntity.getEnv(),
                backEntity.getEc2Url()
                );
    }

    public BackEnd toDomain(RegisterRequestDto dto) {
        return new BackEnd(
                dto.loginPath(),
                dto.os(),
                dto.env(),
                dto.ec2Url()
        );
    }

    public BackEntity toEntity(BackEnd backEnd, TeamEntity teamEntity) {
        return BackEntity.builder()
                .teamEntity(teamEntity)
                .loginPath(backEnd.getLoginPath())
                .os(backEnd.getOs())
                .env(backEnd.getEnv())
                .build();
    }
}

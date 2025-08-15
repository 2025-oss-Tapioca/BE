package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.adapter.out.entity.project.BackEntity;
import com.tapioca.BE.adapter.out.entity.user.TeamEntity;
import com.tapioca.BE.application.dto.request.common.ReadServerRequestDto;
import com.tapioca.BE.application.dto.request.back.RegisterRequestDto;
import com.tapioca.BE.domain.model.project.BackEnd;
import org.springframework.stereotype.Component;

@Component
public class BackEndMapper {
    public BackEnd toDomain(BackEntity backEntity){
        return new BackEnd(
                backEntity.getTeamEntity().getCode(),
                backEntity.getLoginPath(),
                backEntity.getEc2Url(),
                backEntity.getAuthToken(),
                backEntity.getOs(),
                backEntity.getEnv()
                );
    }

    public BackEnd toDomain(RegisterRequestDto dto) {
        return new BackEnd(
                dto.teamCode(),
                dto.loginPath(),
                dto.ec2Url(),
                dto.authToken(),
                dto.os(),
                dto.env()
        );
    }

    public BackEnd toDomain(ReadServerRequestDto dto) {
        return new BackEnd(
                dto.teamCode(),
                null,
                null,
                null,
                null,
                null
        );
    }

    public BackEntity toEntity(BackEnd backEnd, TeamEntity teamEntity) {
        return BackEntity.builder()
                .teamEntity(teamEntity)
                .loginPath(backEnd.getLoginPath())
                .ec2Url(backEnd.getEc2Url())
                .authToken(backEnd.getAuthToken())
                .os(backEnd.getOs())
                .env(backEnd.getEnv())
                .build();
    }

    // BackEnd update
    public BackEntity toEntity(BackEnd updated, BackEntity existing, TeamEntity teamEntity) {
        return BackEntity.builder()
                .id(existing.getId())
                .teamEntity(teamEntity)
                .loginPath(updated.getLoginPath())
                .ec2Url(updated.getEc2Url())
                .authToken(updated.getAuthToken())
                .os(updated.getOs())
                .env(updated.getEnv())
                .build();
    }
}

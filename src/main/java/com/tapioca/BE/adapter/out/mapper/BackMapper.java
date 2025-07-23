package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.adapter.out.entity.BackEntity;
import com.tapioca.BE.application.dto.request.server.RegisterRequestDto;
import com.tapioca.BE.domain.model.BackEnd;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BackMapper {
    public BackEnd toDomain(RegisterRequestDto registerRequestDto) {
        return new BackEnd(
                registerRequestDto.ec2Host(),
                registerRequestDto.authToken(),
                registerRequestDto.os(),
                registerRequestDto.env()
        );
    }

    public BackEntity toEntity(BackEnd backEnd) {
        return BackEntity.builder()
                .ec2Host(backEnd.getEc2Host())
                .authToken(backEnd.getAuthToken())
                .os(backEnd.getOs())
                .env(backEnd.getEnv())
                .build();
    }
}

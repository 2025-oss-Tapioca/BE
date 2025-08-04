package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.adapter.out.entity.DbEntity;
import com.tapioca.BE.adapter.out.entity.TeamEntity;
import com.tapioca.BE.application.dto.request.db.RegisterRequestDto;
import com.tapioca.BE.domain.model.DB;
import org.springframework.stereotype.Component;

@Component
public class DbMapper {
    public DB toDomain(RegisterRequestDto registerRequestDto) {
        return new DB(
                null,
                registerRequestDto.teamId(),
                registerRequestDto.dbAddress(),
                registerRequestDto.dbUser(),
                registerRequestDto.password(),
                registerRequestDto.dbName(),
                registerRequestDto.dbPort(),
                registerRequestDto.rdsInstanceId(),
                registerRequestDto.awsRegion(),
                registerRequestDto.awsAccessKey(),
                registerRequestDto.awsSecretKey()
        );
    }

    public DbEntity toEntity(DB db, TeamEntity teamEntity) {
        return DbEntity.builder()
                .id(db.getId())
                .teamEntity(teamEntity)
                .address(db.getDbAddress())
                .user(db.getDbUser())
                .password(db.getPassword())
                .name(db.getDbName())
                .port(db.getDbPort())
                .rdsInstanceId(db.getRdsInstanceId())
                .awsRegion(db.getAwsRegion())
                .awsAccessKey(db.getAwsAccessKey())
                .awsSecretKey(db.getAwsSecretKey())
                .build();
    }
}

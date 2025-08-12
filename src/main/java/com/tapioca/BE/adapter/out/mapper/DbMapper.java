package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.adapter.out.entity.project.DbEntity;
import com.tapioca.BE.adapter.out.entity.user.TeamEntity;
import com.tapioca.BE.application.dto.request.db.RegisterRequestDto;
import com.tapioca.BE.domain.model.project.DB;
import org.springframework.stereotype.Component;

@Component
public class DbMapper {
    public DB toDomain(RegisterRequestDto registerRequestDto) {
        return new DB(
                registerRequestDto.teamCode(),
                registerRequestDto.dbAddress(),
                registerRequestDto.dbUser(),
                registerRequestDto.password(),
                registerRequestDto.dbName(),
                registerRequestDto.dbPort(),
                registerRequestDto.rdsInstanceId(),
                registerRequestDto.awsRegion(),
                registerRequestDto.roleArn()
        );
    }

    public DbEntity toEntity(DB db, TeamEntity teamEntity) {
        return DbEntity.builder()
                .teamEntity(teamEntity)
                .address(db.getDbAddress())
                .user(db.getDbUser())
                .password(db.getPassword())
                .name(db.getDbName())
                .port(db.getDbPort())
                .rdsInstanceId(db.getRdsInstanceId())
                .awsRegion(db.getAwsRegion())
                .roleArn(db.getRoleArn())
                .build();
    }
}

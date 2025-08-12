package com.tapioca.BE.application.dto.response.db;

import java.util.UUID;

public record RegisterResponseDto(
        String teamId,
        String dbAddress,
        String dbUser,
        String password,
        String dbName,
        String dbPort,
        String rdsInstanceId,
        String awsRegion,
        String roleArn
) {
}

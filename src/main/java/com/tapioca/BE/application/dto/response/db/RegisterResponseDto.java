package com.tapioca.BE.application.dto.response.db;

import java.util.UUID;

public record RegisterResponseDto(
        // UUID id,
        UUID teamId,
        String dbAddress,
        String dbUser,
        String password,
        String dbName,
        String dbPort,
        String rdsInstanceId,
        String awsRegion,
        String awsAccessKey,
        String awsSecretKey
) {
}

package com.tapioca.BE.application.dto.request.db;

public record RegisterRequestDto(
        String teamCode,
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

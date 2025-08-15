package com.tapioca.BE.application.dto.response.server;

public record ReadServerResponseDto(
        String teamCode,
        FrontResponse front,
        BackResponse back,
        DbResponse db
) {
    public record FrontResponse(
            String ec2Host, String authToken, String entryPoint,
            String os, String env, String protocol
    ) {}

    public record BackResponse(
            String loginPath, String ec2Url,
            String authToken, String os, String env
    ) {}

    public record DbResponse(
            String dbAddress, String dbUser,
            String password, String dbName, String dbPort,
            String rdsInstanceId, String awsRegion, String roleArn
    ) {}
}

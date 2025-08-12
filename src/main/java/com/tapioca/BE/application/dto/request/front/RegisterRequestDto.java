package com.tapioca.BE.application.dto.request.front;

import java.util.UUID;

public record RegisterRequestDto(
        String teamCode,
        String ec2Host,
        String authToken,
        String entryPoint,
        String os,
        String env,
        String protocol
) {
}

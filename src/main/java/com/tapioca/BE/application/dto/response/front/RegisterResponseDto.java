package com.tapioca.BE.application.dto.response.front;

import java.util.UUID;

public record RegisterResponseDto(
        String teamCode,
        String ec2Host,
        String entryPoint,
        String os,
        String env,
        String protocol
) {
}

package com.tapioca.BE.application.dto.response.front;

import java.util.UUID;

public record RegisterResponseDto(
        UUID teamId,
        String ec2Host,
        String entryPoint,
        String os,
        String env,
        String protocol
) {
}

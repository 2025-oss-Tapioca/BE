package com.tapioca.BE.application.dto.request.front;

import java.util.UUID;

public record RegisterRequestDto(
        UUID teamId,
        String ec2Host,
        String entryPoint,
        String os,
        String env,
        String protocol
) {
}

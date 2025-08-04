package com.tapioca.BE.application.dto.request.back;

import java.util.UUID;

public record RegisterRequestDto(
        // UUID id,
        UUID teamId,
        String ec2Host,
        String ec2Url,
        String authToken,
        String os,
        String env
) {
}

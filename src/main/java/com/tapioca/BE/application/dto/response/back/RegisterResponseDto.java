package com.tapioca.BE.application.dto.response.back;

import java.util.UUID;

public record RegisterResponseDto(
        // UUID id,
        UUID teamId,
        String ec2Host,
        String ec2Url,
        String authToken,
        String os,
        String env

) {
}

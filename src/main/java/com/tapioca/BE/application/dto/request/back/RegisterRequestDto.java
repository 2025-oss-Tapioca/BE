package com.tapioca.BE.application.dto.request.back;

public record RegisterRequestDto(
        // UUID id,
        // UUID teamId,
        String ec2Host,
        String authToken,
        String os,
        String env
) {
}

package com.tapioca.BE.application.dto.request.front;

public record RegisterRequestDto(
        String ec2Host,
        String entryPoint,
        String os,
        String env,
        String protocol
) {
}

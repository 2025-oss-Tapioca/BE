package com.tapioca.BE.application.dto.request.server;

public record RegisterRequestDto(
        String ec2Host,
        String authToken,
        String os,
        String env
) {
}

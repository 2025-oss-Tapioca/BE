package com.tapioca.BE.application.dto.request.back;

import java.util.UUID;

public record RegisterRequestDto(
        String teamCode,
        String loginPath,
        String ec2Url,
        String authToken,
        String os,
        String env
) {
}

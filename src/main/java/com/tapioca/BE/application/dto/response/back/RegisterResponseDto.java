package com.tapioca.BE.application.dto.response.back;

import java.util.UUID;

public record RegisterResponseDto(
        String loginPath,
        String ec2Url,
        String authToken,
        String os,
        String env

) {
}

package com.tapioca.BE.application.dto.response.back;

import java.util.UUID;

public record RegisterResponseDto(
        String loginPath,
        String ec2Url,
        String os,
        String env

) {
}

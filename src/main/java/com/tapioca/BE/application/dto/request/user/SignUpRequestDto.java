package com.tapioca.BE.application.dto.request.user;

public record SignUpRequestDto(
        String loginId,
        String email,
        String name,
        String password
) {
}

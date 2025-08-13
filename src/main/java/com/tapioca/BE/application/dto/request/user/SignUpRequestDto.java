package com.tapioca.BE.application.dto.request.user;

public record SignUpRequestDto(
        String userId,
        String email,
        String name,
        String password
) {
}

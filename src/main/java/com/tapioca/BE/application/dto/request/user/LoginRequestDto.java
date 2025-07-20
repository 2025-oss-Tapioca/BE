package com.tapioca.BE.application.dto.request.user;

public record LoginRequestDto(
        String userId,
        String name,
        String password
) {
}

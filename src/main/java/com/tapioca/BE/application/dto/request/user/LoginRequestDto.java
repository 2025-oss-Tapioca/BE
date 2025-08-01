package com.tapioca.BE.application.dto.request.user;

public record LoginRequestDto(
        String loginId,
        String password
) {
}

package com.tapioca.BE.application.dto.request.db;

import java.util.UUID;

public record RegisterRequestDto(
        UUID id,
        UUID teamId,
        String dbAddress,
        String dbUser,
        String password,
        String dbName,
        String dbPort
) {
}

package com.tapioca.BE.domain.port.in.usecase.db;

import com.tapioca.BE.application.dto.request.db.RegisterRequestDto;
import com.tapioca.BE.config.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.UUID;

public interface DbRegisterUseCase {
    public void register(UUID userId, RegisterRequestDto dbRequestDto);
}

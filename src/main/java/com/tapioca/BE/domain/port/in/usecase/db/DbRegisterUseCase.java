package com.tapioca.BE.domain.port.in.usecase.db;

import com.tapioca.BE.application.dto.request.db.RegisterRequestDto;
import com.tapioca.BE.application.dto.response.db.RegisterResponseDto;
import com.tapioca.BE.config.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.UUID;

public interface DbRegisterUseCase {
    public RegisterResponseDto register(RegisterRequestDto dbRequestDto);
}

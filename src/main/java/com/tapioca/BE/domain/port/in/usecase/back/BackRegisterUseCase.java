package com.tapioca.BE.domain.port.in.usecase.back;

import com.tapioca.BE.application.dto.request.back.RegisterRequestDto;
import com.tapioca.BE.application.dto.response.back.RegisterResponseDto;

import java.util.UUID;

public interface BackRegisterUseCase {
    public RegisterResponseDto register(RegisterRequestDto backRequestDto, String teamCode);
}

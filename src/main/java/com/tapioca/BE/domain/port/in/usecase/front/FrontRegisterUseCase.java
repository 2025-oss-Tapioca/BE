package com.tapioca.BE.domain.port.in.usecase.front;

import com.tapioca.BE.application.dto.request.front.RegisterRequestDto;

import java.util.UUID;

public interface FrontRegisterUseCase {
    public void register(UUID userId, RegisterRequestDto frontRequestDto);
}

package com.tapioca.BE.domain.port.in.usecase.front;

import com.tapioca.BE.application.dto.request.front.RegisterRequestDto;
import com.tapioca.BE.application.dto.response.front.RegisterResponseDto;

public interface FrontUpdateUseCase {
    public RegisterResponseDto update(RegisterRequestDto frontRequestDto);
}

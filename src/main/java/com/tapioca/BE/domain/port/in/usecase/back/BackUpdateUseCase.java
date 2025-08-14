package com.tapioca.BE.domain.port.in.usecase.back;

import com.tapioca.BE.application.dto.request.back.RegisterRequestDto;
import com.tapioca.BE.application.dto.response.back.RegisterResponseDto;

public interface BackUpdateUseCase {
    public RegisterResponseDto update(RegisterRequestDto registerRequestDto);
}

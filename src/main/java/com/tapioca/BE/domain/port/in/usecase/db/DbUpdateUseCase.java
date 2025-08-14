package com.tapioca.BE.domain.port.in.usecase.db;

import com.tapioca.BE.application.dto.request.db.RegisterRequestDto;
import com.tapioca.BE.application.dto.response.db.RegisterResponseDto;

public interface DbUpdateUseCase {
    public RegisterResponseDto update(RegisterRequestDto registerRequestDto);
}

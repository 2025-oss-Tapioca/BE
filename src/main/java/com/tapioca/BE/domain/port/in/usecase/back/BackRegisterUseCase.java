package com.tapioca.BE.domain.port.in.usecase.back;

import com.tapioca.BE.application.dto.request.back.RegisterRequestDto;

public interface BackRegisterUseCase {
    public void register(RegisterRequestDto backRequestDto);
}

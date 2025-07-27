package com.tapioca.BE.domain.port.in.usecase.db;

import com.tapioca.BE.application.dto.request.db.RegisterRequestDto;

public interface DbRegisterUseCase {
    public void register(RegisterRequestDto dbRequestDto);
}

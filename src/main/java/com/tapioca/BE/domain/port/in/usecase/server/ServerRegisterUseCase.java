package com.tapioca.BE.domain.port.in.usecase.server;

import com.tapioca.BE.application.dto.request.server.RegisterRequestDto;

public interface ServerRegisterUseCase {
    void register(RegisterRequestDto serverRequestDto);
}

package com.tapioca.BE.domain.port.in.usecase.server;

import com.tapioca.BE.application.dto.request.common.ReadServerRequestDto;
import com.tapioca.BE.application.dto.response.server.ReadServerResponseDto;

import java.util.UUID;

public interface ServerReadUseCase {
    public ReadServerResponseDto read(UUID userId, String teamCode);
}

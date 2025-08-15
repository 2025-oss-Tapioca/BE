package com.tapioca.BE.domain.port.in.usecase.server;

import com.tapioca.BE.application.dto.request.common.ReadServerRequestDto;
import com.tapioca.BE.application.dto.response.server.ReadServerResponseDto;

public interface ServerReadUseCase {
    public ReadServerResponseDto read(String teamCode);
}

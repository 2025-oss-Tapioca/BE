package com.tapioca.BE.domain.port.in.usecase.erd;

import com.tapioca.BE.application.dto.request.erd.UpdateErdRequestDto;
import com.tapioca.BE.application.dto.response.erd.ErdResponseDto;

import java.util.UUID;

public interface ErdUseCase {
    ErdResponseDto getErd(UUID userId);
    ErdResponseDto updateErd(UUID userId, UpdateErdRequestDto updateErdRequestDto);
}

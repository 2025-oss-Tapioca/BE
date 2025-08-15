package com.tapioca.BE.domain.port.in.usecase.back;

import com.tapioca.BE.application.dto.request.common.ReadServerRequestDto;

public interface BackDeleteUseCase {
    public void delete(ReadServerRequestDto readServerRequestDto);
}

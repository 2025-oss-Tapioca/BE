package com.tapioca.BE.domain.port.in.usecase.db;

import com.tapioca.BE.application.dto.request.common.ReadServerRequestDto;

public interface DbDeleteUseCase {
    public void delete(ReadServerRequestDto readServerRequestDto);
}

package com.tapioca.BE.domain.port.in.usecase.erd;

import com.tapioca.BE.application.dto.request.erd.CreateRequestDto;

public interface ErdCreateUseCase {
    public void createErd(CreateRequestDto createRequestDto);
}

package com.tapioca.BE.domain.port.in.usecase.back;

import com.tapioca.BE.application.dto.request.back.DeleteRequestDto;

public interface BackDeleteUseCase {
    public void delete(DeleteRequestDto deleteRequestDto);
}

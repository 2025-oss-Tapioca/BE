package com.tapioca.BE.domain.port.in.usecase.front;

import com.tapioca.BE.application.dto.request.common.DeleteServerRequestDto;

public interface FrontDeleteUseCase {
    public void delete(DeleteServerRequestDto deleteServerRequestDto);
}

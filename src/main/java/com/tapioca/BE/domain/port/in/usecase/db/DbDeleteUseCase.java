package com.tapioca.BE.domain.port.in.usecase.db;

import com.tapioca.BE.application.dto.request.common.DeleteServerRequestDto;

public interface DbDeleteUseCase {
    public void delete(DeleteServerRequestDto deleteServerRequestDto);
}

package com.tapioca.BE.domain.port.in.usecase.github;

import com.tapioca.BE.application.dto.request.common.ReadServerRequestDto;

public interface GitHubDeleteUseCase {
    public void delete(ReadServerRequestDto readServerRequestDto);
}

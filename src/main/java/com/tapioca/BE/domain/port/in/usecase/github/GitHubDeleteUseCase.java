package com.tapioca.BE.domain.port.in.usecase.github;

import com.tapioca.BE.application.dto.request.common.DeleteServerRequestDto;

public interface GitHubDeleteUseCase {
    public void delete(DeleteServerRequestDto deleteServerRequestDto);
}

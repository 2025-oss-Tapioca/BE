package com.tapioca.BE.domain.port.in.usecase.gpt;

import com.tapioca.BE.application.dto.request.gpt.UserInputRequestDto;

import java.util.UUID;


public interface GptTypeUseCase {
    public Object gptTypeRequest(
            UserInputRequestDto GptRequestDto,
            UUID teamId
    );
}

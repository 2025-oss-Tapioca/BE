package com.tapioca.BE.domain.port.in.usecase.gpt;

import com.tapioca.BE.application.dto.request.gpt.GptRequestDto;
import com.tapioca.BE.application.dto.response.gpt.GptResponseDto;

public interface GptRequestUseCase {
    GptResponseDto gptRequest(GptRequestDto requestDto);
}

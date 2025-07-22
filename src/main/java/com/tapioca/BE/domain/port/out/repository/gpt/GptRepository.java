package com.tapioca.BE.domain.port.out.repository.gpt;

import com.tapioca.BE.application.dto.request.gpt.GptRequestDto;
import com.tapioca.BE.application.dto.response.gpt.GptResponseDto;

public interface GptRepository {
    public GptResponseDto gptRequest(GptRequestDto gptRequestDto);
}

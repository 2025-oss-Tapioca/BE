package com.tapioca.BE.domain.port.in.usecase.gpt;

import com.tapioca.BE.application.dto.request.gpt.GptRequestDto;
import com.tapioca.BE.application.dto.response.gpt.GptResponseDto;
import org.springframework.http.ResponseEntity;


public interface GptUseCase {
    public ResponseEntity<GptResponseDto> gptRequest(GptRequestDto GptRequestDto);
}

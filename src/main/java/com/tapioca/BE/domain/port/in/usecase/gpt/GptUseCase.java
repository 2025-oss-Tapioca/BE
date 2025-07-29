package com.tapioca.BE.domain.port.in.usecase.gpt;

import com.tapioca.BE.application.dto.request.gpt.GptRequestDto;
import com.tapioca.BE.application.dto.response.gpt.GptResponseDto;
import com.tapioca.BE.config.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;

import java.util.UUID;


public interface GptUseCase {
    public Object gptRequest(
            GptRequestDto GptRequestDto, UUID teamId
            ,String authorizationHeader
    );
}

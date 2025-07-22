package com.tapioca.BE.application.service.gpt;

import com.tapioca.BE.application.dto.request.gpt.GptRequestDto;
import com.tapioca.BE.application.dto.response.gpt.GptResponseDto;
import com.tapioca.BE.domain.port.in.usecase.gpt.GptRequestUseCase;
import com.tapioca.BE.domain.port.out.repository.gpt.GptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GptRequestService implements GptRequestUseCase {
    private final GptRepository gptRepository;

    @Override
    public GptResponseDto gptRequest(GptRequestDto gptRequestDto) {
        return gptRepository.gptRequest(gptRequestDto);
    }
}

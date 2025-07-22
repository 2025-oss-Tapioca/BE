package com.tapioca.BE.adapter.in.controller;

import com.tapioca.BE.application.dto.request.gpt.GptRequestDto;
import com.tapioca.BE.application.dto.response.gpt.GptResponseDto;
import com.tapioca.BE.config.common.CommonResponseDto;
import com.tapioca.BE.domain.port.in.usecase.gpt.GptRequestUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GPTController {
    private final GptRequestUseCase gptRequestUseCase;
    @PostMapping("/api/gpt-request")
    public CommonResponseDto<?> gptRequest(@RequestBody GptRequestDto gptRequestDto) {
        GptResponseDto response = gptRequestUseCase.gptRequest(gptRequestDto);
        return CommonResponseDto.ok(response);
    }
}

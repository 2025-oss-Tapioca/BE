package com.tapioca.BE.adapter.in.gpt;

import com.tapioca.BE.application.dto.request.gpt.GptRequestDto;
import com.tapioca.BE.config.common.CommonResponseDto;
import com.tapioca.BE.domain.port.in.usecase.gpt.GptUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GptController {

    private final GptUseCase gptUseCase;

    @PostMapping
    public CommonResponseDto<?> requestToGpt(@RequestBody GptRequestDto gptRequestDto){
        return CommonResponseDto.ok(gptUseCase.gptRequest(gptRequestDto));
    }
}

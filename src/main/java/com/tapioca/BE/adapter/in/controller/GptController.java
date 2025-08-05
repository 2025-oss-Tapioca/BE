package com.tapioca.BE.adapter.in.controller;

import com.tapioca.BE.application.dto.request.gpt.UserInputRequestDto;
import com.tapioca.BE.config.common.CommonResponseDto;
import com.tapioca.BE.domain.port.in.usecase.gpt.GptTypeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GptController {

    private final GptTypeUseCase gptTypeUseCase;

    @PostMapping("/{teamId}")
    public CommonResponseDto<?> requestToGpt(
            @RequestBody UserInputRequestDto userInputRequestDto,
            @PathVariable UUID teamId
    ){
        return CommonResponseDto.ok(gptTypeUseCase.gptTypeRequest(userInputRequestDto,teamId));
    }
}

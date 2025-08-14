package com.tapioca.BE.adapter.in.controller;

import com.tapioca.BE.application.dto.request.front.RegisterRequestDto;
import com.tapioca.BE.config.common.CommonResponseDto;
import com.tapioca.BE.domain.port.in.usecase.front.FrontRegisterUseCase;
import com.tapioca.BE.domain.port.in.usecase.front.FrontUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/server/front")
@RequiredArgsConstructor
public class FrontController {

    private final FrontRegisterUseCase frontRegisterUseCase;
    private final FrontUpdateUseCase frontUpdateUseCase;

    @PostMapping
    public CommonResponseDto<?> frontRegister(
            @RequestBody RegisterRequestDto registerRequestDto
    ) {
        frontRegisterUseCase.register(registerRequestDto);
        return CommonResponseDto.created(null);
    }

    @PatchMapping
    public CommonResponseDto<?> frontUpdate(
            @RequestBody RegisterRequestDto registerRequestDto
    ) {
        return CommonResponseDto.ok(frontUpdateUseCase.update(registerRequestDto));
    }
}

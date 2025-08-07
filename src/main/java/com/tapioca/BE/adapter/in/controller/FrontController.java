package com.tapioca.BE.adapter.in.controller;

import com.tapioca.BE.application.dto.request.front.RegisterRequestDto;
import com.tapioca.BE.config.common.CommonResponseDto;
import com.tapioca.BE.domain.port.in.usecase.front.FrontRegisterUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FrontController {

    private final FrontRegisterUseCase frontRegisterUseCase;

    @PostMapping("/front")
    public CommonResponseDto<?> frontRegister(
            @RequestBody RegisterRequestDto registerRequestDto
    ) {
        frontRegisterUseCase.register(registerRequestDto);
        return CommonResponseDto.created(null);
    }
}

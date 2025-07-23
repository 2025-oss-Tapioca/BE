package com.tapioca.BE.adapter.in.controller;

import com.tapioca.BE.application.dto.request.server.RegisterRequestDto;
import com.tapioca.BE.config.common.CommonResponseDto;
import com.tapioca.BE.domain.port.in.usecase.server.ServerRegisterUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ServerController {
    private final ServerRegisterUseCase serverRegisterUseCase;

    @PostMapping("/signup/deploy/back")
    public CommonResponseDto<?> serverRegister(
            @RequestBody RegisterRequestDto registerRequestDto
    ) {
        // dto에서 return type 확인하기
        return CommonResponseDto.ok(serverRegisterUseCase.register(registerRequestDto));
    }
}

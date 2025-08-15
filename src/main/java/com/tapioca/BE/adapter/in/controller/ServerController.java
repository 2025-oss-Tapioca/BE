package com.tapioca.BE.adapter.in.controller;

import com.tapioca.BE.application.dto.request.common.ReadServerRequestDto;
import com.tapioca.BE.config.common.CommonResponseDto;
import com.tapioca.BE.domain.port.in.usecase.server.ServerReadUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/server")
@RequiredArgsConstructor
public class ServerController {

    private final ServerReadUseCase serverReadUseCase;

    @GetMapping
    public CommonResponseDto<?> serverRead(
            @RequestBody ReadServerRequestDto readServerRequestDto
    ) {
        return CommonResponseDto.ok(serverReadUseCase.read(readServerRequestDto));
    }
}

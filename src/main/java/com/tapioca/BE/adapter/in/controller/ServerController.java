package com.tapioca.BE.adapter.in.controller;

import com.tapioca.BE.application.dto.request.common.ReadServerRequestDto;
import com.tapioca.BE.config.common.CommonResponseDto;
import com.tapioca.BE.domain.port.in.usecase.server.ServerReadUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/server")
@RequiredArgsConstructor
public class ServerController {

    private final ServerReadUseCase serverReadUseCase;

    @GetMapping("/{teamCode}")
    public CommonResponseDto<?> serverRead(@PathVariable String teamCode) {
        return CommonResponseDto.ok(serverReadUseCase.read(teamCode));
    }
}

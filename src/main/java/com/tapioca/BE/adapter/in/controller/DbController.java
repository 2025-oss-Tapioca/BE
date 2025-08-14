package com.tapioca.BE.adapter.in.controller;

import com.tapioca.BE.application.dto.request.db.RegisterRequestDto;
import com.tapioca.BE.config.common.CommonResponseDto;
import com.tapioca.BE.config.security.CustomUserDetails;
import com.tapioca.BE.domain.port.in.usecase.db.DbRegisterUseCase;
import com.tapioca.BE.domain.port.in.usecase.db.DbUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/server/db")
@RequiredArgsConstructor
public class DbController {

    private final DbRegisterUseCase dbRegisterUseCase;
    private final DbUpdateUseCase dbUpdateUseCase;

    @PostMapping
    public CommonResponseDto<?> dbRegister(
            @RequestBody RegisterRequestDto registerRequestDto
    ) {
        dbRegisterUseCase.register(registerRequestDto);
        return CommonResponseDto.ok(null);
    }

    @PatchMapping
    public CommonResponseDto<?> dbUpdate(
            @RequestBody RegisterRequestDto registerRequestDto
    ) {
        dbUpdateUseCase.update(registerRequestDto);
        return CommonResponseDto.ok(dbUpdateUseCase.update(registerRequestDto));
    }
}

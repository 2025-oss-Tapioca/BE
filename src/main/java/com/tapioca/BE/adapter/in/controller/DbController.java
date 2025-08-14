package com.tapioca.BE.adapter.in.controller;

import com.tapioca.BE.application.dto.request.common.DeleteServerRequestDto;
import com.tapioca.BE.application.dto.request.db.RegisterRequestDto;
import com.tapioca.BE.config.common.CommonResponseDto;
import com.tapioca.BE.config.security.CustomUserDetails;
import com.tapioca.BE.domain.port.in.usecase.db.DbDeleteUseCase;
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
    private final DbDeleteUseCase dbDeleteUseCase;

    @PostMapping
    public CommonResponseDto<?> dbRegister(
            @RequestBody RegisterRequestDto registerRequestDto
    ) {
        return CommonResponseDto.ok(dbRegisterUseCase.register(registerRequestDto));
    }

    @DeleteMapping
    public CommonResponseDto<?> dbDelete(
            @RequestBody DeleteServerRequestDto deleteServerRequestDto
    ) {
        dbDeleteUseCase.delete(deleteServerRequestDto);
        return CommonResponseDto.noContent();
    }

    @PatchMapping
    public CommonResponseDto<?> dbUpdate(
            @RequestBody RegisterRequestDto registerRequestDto
    ) {
        dbUpdateUseCase.update(registerRequestDto);
        return CommonResponseDto.ok(dbUpdateUseCase.update(registerRequestDto));
    }
}

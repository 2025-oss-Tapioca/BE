package com.tapioca.BE.adapter.in.controller;

import com.tapioca.BE.application.dto.request.back.RegisterRequestDto;
import com.tapioca.BE.config.common.CommonResponseDto;
import com.tapioca.BE.config.security.CustomUserDetails;
import com.tapioca.BE.domain.port.in.usecase.back.BackDeleteUseCase;
import com.tapioca.BE.domain.port.in.usecase.back.BackRegisterUseCase;
import com.tapioca.BE.domain.port.in.usecase.back.BackUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/server/back")
@RequiredArgsConstructor
public class BackController {
    private final BackRegisterUseCase backRegisterUseCase;
    private final BackDeleteUseCase backDeleteUseCase;
    private final BackUpdateUseCase backUpdateUseCase;

    // Back server 등록
    @PostMapping
    public CommonResponseDto<?> backRegister(
            @RequestBody RegisterRequestDto registerRequestDto
    ) {
        backRegisterUseCase.register(registerRequestDto);
        return CommonResponseDto.created(null);
    }

    @DeleteMapping
    public CommonResponseDto<?> backDelete(
            @PathVariable String teamCode
    ) {
        backDeleteUseCase.delete(teamCode);
        return CommonResponseDto.noContent();
    }

    @PatchMapping
    public CommonResponseDto<?> backUpdate(
            @RequestBody RegisterRequestDto registerRequestDto
    ) {
        return CommonResponseDto.ok(backUpdateUseCase.update(registerRequestDto));
    }
}

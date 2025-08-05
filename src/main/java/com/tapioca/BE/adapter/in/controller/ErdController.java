package com.tapioca.BE.adapter.in.controller;

import com.tapioca.BE.application.dto.request.erd.UpdateErdRequestDto;
import com.tapioca.BE.config.common.CommonResponseDto;
import com.tapioca.BE.config.security.CustomUserDetails;
import com.tapioca.BE.domain.port.in.usecase.erd.ErdUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/erd")
@RequiredArgsConstructor
public class ErdController {
    private final ErdUseCase erdUseCase;

    @GetMapping("")
    public CommonResponseDto<?> getErd(@AuthenticationPrincipal CustomUserDetails user) {
        return CommonResponseDto.ok(erdUseCase.getErd(user.getUserId()));
    }

    @PostMapping("/update")
    public CommonResponseDto<?> updateDigrams(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody UpdateErdRequestDto updateDiagramsRequestDto)
    {
        return CommonResponseDto.ok(erdUseCase.updateErd(user.getUserId(), updateDiagramsRequestDto));
    }
}

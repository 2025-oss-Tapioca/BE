package com.tapioca.BE.adapter.in.controller;

import com.tapioca.BE.application.dto.request.back.RegisterRequestDto;
import com.tapioca.BE.config.common.CommonResponseDto;
import com.tapioca.BE.config.security.CustomUserDetails;
import com.tapioca.BE.domain.port.in.usecase.back.BackRegisterUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BackController {
    private final BackRegisterUseCase backRegisterUseCase;

    @PostMapping("/api/signup/deploy/back")
    public CommonResponseDto<?> backRegister(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody RegisterRequestDto registerRequestDto
    ) {
        backRegisterUseCase.register(user.getUserId(), registerRequestDto);
        return CommonResponseDto.created(null);
    }
}

package com.tapioca.BE.adapter.in.controller;

import com.tapioca.BE.application.command.erd.UpdateDiagramsCommand;
import com.tapioca.BE.application.dto.request.erd.UpdateDiagramsRequestDto;
import com.tapioca.BE.application.dto.response.erd.ErdResponseDto;
import com.tapioca.BE.config.common.CommonResponseDto;
import com.tapioca.BE.config.security.CustomUserDetails;
import com.tapioca.BE.domain.port.in.usecase.erd.ErdUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/erd")
@RequiredArgsConstructor
public class ErdController {
    private final ErdUseCase erdUseCase;

    @PostMapping("/update/diagrams")
    public CommonResponseDto<?> updateDiagrams(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody UpdateDiagramsRequestDto requestDto
    ){
        UpdateDiagramsCommand cmd = requestDto.toCommand(user.getUserId());
        ErdResponseDto result = erdUseCase.updateDiagrams(cmd);
        return CommonResponseDto.ok(result);
    }
}

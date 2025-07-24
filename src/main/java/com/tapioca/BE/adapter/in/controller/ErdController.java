package com.tapioca.BE.adapter.in.controller;

import com.tapioca.BE.application.command.erd.CreateDiagramCommand;
import com.tapioca.BE.application.dto.request.erd.CreateDiagramRequestDto;
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

    @PostMapping("/create/diagram")
    public CommonResponseDto<?> createDiagram(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody CreateDiagramRequestDto requestDto
    ){
        CreateDiagramCommand command = new CreateDiagramCommand(
                user.getUserId(),
                requestDto.name(),
                requestDto.toAttributeCommands()
        );
    }
}

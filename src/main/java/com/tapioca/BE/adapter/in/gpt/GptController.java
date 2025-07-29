package com.tapioca.BE.adapter.in.gpt;

import com.tapioca.BE.application.dto.request.gpt.GptRequestDto;
import com.tapioca.BE.config.common.CommonResponseDto;
import com.tapioca.BE.config.security.CustomUserDetails;
import com.tapioca.BE.domain.port.in.usecase.gpt.GptUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GptController {

    private final GptUseCase gptUseCase;

    @PostMapping("/{teamId}")
    public CommonResponseDto<?> requestToGpt(
            @RequestBody GptRequestDto gptRequestDto,
            @PathVariable UUID teamId,
            @RequestHeader("Authorization") String authorizationHeader
    ){
        return CommonResponseDto.ok(gptUseCase.gptRequest(gptRequestDto,teamId,authorizationHeader));
    }
}

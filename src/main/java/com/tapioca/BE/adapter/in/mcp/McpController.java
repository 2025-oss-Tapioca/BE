package com.tapioca.BE.adapter.in.mcp;

import com.tapioca.BE.application.dto.request.mcp.McpRequestDto;
import com.tapioca.BE.config.common.CommonResponseDto;
import com.tapioca.BE.domain.port.in.usecase.mcp.McpUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class McpController {

    private final McpUseCase mcpUseCase;

    @PostMapping
    public CommonResponseDto<?> mcpRequest(@RequestBody McpRequestDto mcpRequestDto){
        return CommonResponseDto.ok(mcpUseCase.sendRequestToMcp(mcpRequestDto));
    }
}

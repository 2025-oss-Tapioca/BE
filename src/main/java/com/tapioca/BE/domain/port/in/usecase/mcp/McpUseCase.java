package com.tapioca.BE.domain.port.in.usecase.mcp;

import com.tapioca.BE.application.dto.request.mcp.McpRequestDto;
import com.tapioca.BE.application.dto.response.mcp.McpResponseDto;
import org.springframework.http.ResponseEntity;

public interface McpUseCase {
    public ResponseEntity<McpResponseDto> sendRequestToMcp(McpRequestDto mcpRequestDto);
}

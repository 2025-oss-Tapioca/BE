package com.tapioca.BE.application.dto.request.mcp;

import java.util.Map;

/**
 * MCP-BE로 전송하는 표준 등록 DTO.
 * - type       : BACKEND | FRONTEND | RDS
 * - internalKey: TYPE|자연키 (teamCode 사용하지 않음)
 * - callbackUrl: MCP-BE가 로그를 푸시할 BE WebSocket 주소
 * - cfg        : 소스별 필요한 필드만 담는 확장 맵
 */
public record McpLogRegisterRequestDto(
        String type,
        String internalKey,
        String callbackUrl,
        Map<String, Object> cfg
) {}

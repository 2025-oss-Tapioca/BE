package com.tapioca.BE.adapter.out.httpImpl;

import com.tapioca.BE.application.dto.request.mcp.McpLogRegisterRequestDto;

/**
 * MCP 서버와의 HTTP 통신을 통해 로그 소스를 등록하는 클라이언트 인터페이스
 */
public interface McpLogClient {

    /**
     * MCP 서버에 로그 등록 요청 정보 DTO (서버 주소, 인증 토큰, 환경 정보 등 포함)
     */
    void registerLogSource(McpLogRegisterRequestDto dto);
}
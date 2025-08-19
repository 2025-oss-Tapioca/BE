package com.tapioca.BE.application.dto.request.mcp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * MCP에 로그 소스 등록을 요청하는 DTO
 * - internalKey는 "<TYPE>|<teamCode>" 형태로 전송 (팀코드 기준으로 전면 통일)
 * - cfg에는 실제 수집에 필요한 서버 세부정보 포함
 */
@Getter
@AllArgsConstructor
public class McpLogRegisterRequestDto {
    private String type;         // BACKEND / FRONTEND / RDS
    private String internalKey;  // 예: BACKEND|TEAM1234
    private String callbackUrl;  // BE WebSocket 주소
    private String teamCode;     // 팀 코드 (WS register 시 그대로 사용)

    @JsonProperty("cfg")
    private Map<String, Object> config; // 수집에 필요한 세부 설정(호스트, 토큰 등)
}
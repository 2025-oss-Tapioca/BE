package com.tapioca.BE.application.dto.response.log;

/**
 * [공용 로그 응답 DTO]
 * - 백엔드/프론트/RDS 상관없이 프론트로 전달하는 표준 형식.
 * - WebSocket 브로드캐스트 및 조회 응답에 공통 사용.
 * 필드 설명
 * - timestamp : ISO-8601 문자열 (예: "2025-08-12T10:15:30")
 * - level     : 로그 레벨 (INFO, WARN, ERROR 등) - 대문자 권장
 * - service   : 서비스/모듈 식별자 (미제공 시 "-")
 * - message   : 로그 본문
 */
public record LogResponseDto(
        String timestamp,
        String level,
        String service,
        String message
) {}

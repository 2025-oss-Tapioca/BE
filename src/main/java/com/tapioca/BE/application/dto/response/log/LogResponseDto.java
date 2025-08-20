package com.tapioca.BE.application.dto.response.log;

/**
 * [공용 로그 응답 DTO]
 * - 백엔드/프론트/RDS 상관없이 프론트로 전달하는 표준 형식.
 * - WebSocket 브로드캐스트 및 조회 응답에 공통 사용.
 */
public record LogResponseDto(
        String timestamp,       // timestamp : ISO-8601 문자열
        String level,           // 로그 레벨 (INFO, WARN, ERROR 등) - 대문자 권장
        String service,         // 서비스/ 모듈 식별자
        String message          // 로그 본문
) {}
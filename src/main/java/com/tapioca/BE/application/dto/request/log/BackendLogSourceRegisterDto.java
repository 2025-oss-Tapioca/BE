package com.tapioca.BE.application.dto.request.log;

/**
 * [로그 전용] 백엔드 소스 등록 DTO.
 * - 기존 back.RegisterRequestDto는 다른 기능에서 사용하므로 건드리지 않는다.
 * - 여기서는 로깅 수집에 필요한 최소 필드만 정의한다.
 */
public record BackendLogSourceRegisterDto(
        String ec2Url,     // 백엔드 서버 접속 대상(호스트/IP/DNS)
        String authToken,  // 인증 토큰/키(예: SSH)
        String os,         // 운영체제(스크립트 분기용 필요 시)
        String env         // 환경(prod/dev 등)
) {}

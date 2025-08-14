package com.tapioca.BE.application.dto.request.log;

/**
 * [로그 전용] 프론트엔드 소스 등록 DTO.
 * - 키 이름은 프론트 입력(JSON) 스펙과 동일하게 유지(예: auth_token)
 */
public record FrontendLogSourceRegisterDto(
        String ec2Host,    // 프론트 서버 호스트/IP/DNS
        String auth_token, // 인증 토큰/키
        String os,         // 운영체제
        String env         // 환경(prod/dev 등)
) {}

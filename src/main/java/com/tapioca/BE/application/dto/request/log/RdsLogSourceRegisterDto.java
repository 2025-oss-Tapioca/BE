package com.tapioca.BE.application.dto.request.log;

/**
 * [로그 전용] RDS 소스 등록 DTO.
 * - CloudWatch 구독 방식을 우선으로 rdsInstanceId/region/roleArn을 필수로 사용.
 * - rdsInstanceId가 없으면 dbAddress를 자연키로 대체할 수 있으므로 함께 받음(선택).
 */
public record RdsLogSourceRegisterDto(
        String rdsInstanceId, // 권장: RDS 인스턴스 식별자
        String awsRegion,     // AWS 리전 (예: ap-northeast-2)
        String roleArn,       // MCP-BE가 AssumeRole할 ARN
        String dbAddress      // 선택: 인스턴스ID 미제공 시 식별 대체
) {}

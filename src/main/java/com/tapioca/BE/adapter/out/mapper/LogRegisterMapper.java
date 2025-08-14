package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.application.dto.request.log.BackendLogSourceRegisterDto;
import com.tapioca.BE.application.dto.request.log.FrontendLogSourceRegisterDto;
import com.tapioca.BE.application.dto.request.log.RdsLogSourceRegisterDto;
import com.tapioca.BE.application.dto.request.mcp.McpLogRegisterRequestDto;

import java.util.HashMap;
import java.util.Map;

/**
 * [등록용 매퍼]
 * 로그 전용 DTO(백/프론트/RDS)에서 필요한 필드만 추려 MCP-BE 표준 DTO로 변환한다.
 * - JsonAlias를 사용하지 않고, DTO에서 받은 필드명을 그대로 사용한다.
 * - teamCode는 사용하지 않는다(요구사항).
 */
public final class LogRegisterMapper {
    private LogRegisterMapper() {}

    /** 백엔드 → MCP 표준 DTO */
    public static McpLogRegisterRequestDto toMcpFromBack(BackendLogSourceRegisterDto d, String callbackUrl) {
        String internalKey = "BACKEND|" + d.ec2Url(); // TYPE|자연키
        Map<String,Object> cfg = new HashMap<>();
        cfg.put("host", d.ec2Url());
        cfg.put("authToken", d.authToken());
        cfg.put("os", d.os());
        cfg.put("env", d.env());
        return new McpLogRegisterRequestDto("BACKEND", internalKey, callbackUrl, cfg);
    }

    /** 프론트 → MCP 표준 DTO */
    public static McpLogRegisterRequestDto toMcpFromFront(FrontendLogSourceRegisterDto d, String callbackUrl) {
        String internalKey = "FRONTEND|" + d.ec2Host();
        Map<String,Object> cfg = new HashMap<>();
        cfg.put("host", d.ec2Host());
        cfg.put("authToken", d.auth_token());
        cfg.put("os", d.os());
        cfg.put("env", d.env());
        return new McpLogRegisterRequestDto("FRONTEND", internalKey, callbackUrl, cfg);
    }

    /** RDS → MCP 표준 DTO (CloudWatch 구독 기준) */
    public static McpLogRegisterRequestDto toMcpFromDb(RdsLogSourceRegisterDto d, String callbackUrl) {
        String natural = (d.rdsInstanceId() != null && !d.rdsInstanceId().isBlank())
                ? d.rdsInstanceId() : d.dbAddress(); // rdsInstanceId 우선, 없으면 dbAddress 사용
        String internalKey = "RDS|" + natural;

        Map<String,Object> cfg = new HashMap<>();
        cfg.put("rdsInstanceId", d.rdsInstanceId());
        cfg.put("awsRegion", d.awsRegion());
        cfg.put("roleArn", d.roleArn());
        // (직접 접속 모드가 필요해지면 아래를 선택적으로 추가)
        // cfg.put("dbAddress", d.dbAddress());
        return new McpLogRegisterRequestDto("RDS", internalKey, callbackUrl, cfg);
    }
}

package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.adapter.out.entity.project.BackEntity;
import com.tapioca.BE.adapter.out.entity.project.FrontEntity;
import com.tapioca.BE.adapter.out.entity.project.DbEntity;
import com.tapioca.BE.application.dto.request.mcp.McpLogRegisterRequestDto;

import java.util.HashMap;
import java.util.Map;

/**
 * [등록용 매퍼]
 * 팀코드로 DB에서 조회한 엔티티(Back/Front/DB) → MCP 표준 DTO로 변환.
 * - 내부 식별 키: "<TYPE>|<teamCode>" 로 전면 통일 (BE/WS와 동일 기준)
 */
public final class LogRegisterMapper {
    private LogRegisterMapper() {}

    /** BackEntity → MCP 표준 DTO (internalKey = BACKEND|<teamCode>) */
    public static McpLogRegisterRequestDto toMcpFromBack(BackEntity e, String teamCode, String callbackUrl) {
        String internalKey = "BACKEND|" + safe(teamCode);
        Map<String, Object> cfg = new HashMap<>();
        cfg.put("host", safe(e.getEc2Url()));
        cfg.put("authToken", safe(e.getAuthToken()));
        cfg.put("os", safe(e.getOs()));
        cfg.put("env", safe(e.getEnv()));
        return new McpLogRegisterRequestDto("BACKEND", internalKey, callbackUrl, safe(teamCode), cfg);
    }

    /** FrontEntity → MCP 표준 DTO (internalKey = FRONTEND|<teamCode>) */
    public static McpLogRegisterRequestDto toMcpFromFront(FrontEntity e, String teamCode, String callbackUrl) {
        String internalKey = "FRONTEND|" + safe(teamCode);
        Map<String, Object> cfg = new HashMap<>();
        cfg.put("host", safe(e.getEc2Host()));
        cfg.put("authToken", safe(e.getAuthToken()));
        cfg.put("entryPoint", safe(e.getEntryPoint()));
        cfg.put("os", safe(e.getOs()));
        cfg.put("env", safe(e.getEnv()));
        cfg.put("protocol", safe(e.getProtocol()));
        return new McpLogRegisterRequestDto("FRONTEND", internalKey, callbackUrl, safe(teamCode), cfg);
    }

    /** DbEntity → MCP 표준 DTO (internalKey = RDS|<teamCode>) */
    public static McpLogRegisterRequestDto toMcpFromDb(DbEntity e, String teamCode, String callbackUrl) {
        String internalKey = "RDS|" + safe(teamCode);
        Map<String, Object> cfg = new HashMap<>();
        // CloudWatch/RDS 구독 경로
        cfg.put("rdsInstanceId", safe(e.getRdsInstanceId()));
        cfg.put("awsRegion", safe(e.getAwsRegion()));
        cfg.put("roleArn", safe(e.getRoleArn()));
        // 직접 접속 모드에 활용
        cfg.put("address", safe(e.getAddress()));
        cfg.put("user", safe(e.getUser()));
        cfg.put("password", safe(e.getPassword()));
        cfg.put("name", safe(e.getName()));
        cfg.put("port", safe(e.getPort()));
        return new McpLogRegisterRequestDto("RDS", internalKey, callbackUrl, safe(teamCode), cfg);
    }

    /* ---------------- helpers ---------------- */
    private static String safe(String s) { return (s == null) ? "" : s; }
}
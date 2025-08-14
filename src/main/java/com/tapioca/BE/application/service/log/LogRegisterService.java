package com.tapioca.BE.application.service.log;

import com.tapioca.BE.adapter.out.httpImpl.WebClientErrorTranslator;
import com.tapioca.BE.adapter.out.mapper.LogRegisterMapper;
import com.tapioca.BE.application.dto.request.log.BackendLogSourceRegisterDto;
import com.tapioca.BE.application.dto.request.log.FrontendLogSourceRegisterDto;
import com.tapioca.BE.application.dto.request.log.RdsLogSourceRegisterDto;
import com.tapioca.BE.application.dto.request.mcp.McpLogRegisterRequestDto;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

// HMAC 계산(JCE 표준) – 별도 유틸/설정 파일 만들지 않음
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * MCP-BE 등록/해제 호출 서비스
 * ─────────────────────────────────────────────────────────────────────────────
 * - WebClient 호출에 X-Signature(HMAC-SHA256)를 “전송 바이트 그대로” 붙인다.
 * - ✅ 서명은 여기(서비스)에서 직렬화 직후 수행 → 전송 바이트와 100% 일치 보장.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LogRegisterService {

    private final WebClient webClient;                 // 기존 Bean 사용
    private final WebClientErrorTranslator translator; // 기존 예외 변환기
    private final ObjectMapper objectMapper;           // 직렬화 바이트 = 전송 바이트

    /* 운영 필수: MCP-BE와 동일한 시크릿. 비어 있으면 개발 모드로 서명 생략. */
    @Value("${logging.shared-secret:}")
    private String sharedSecret;

    /* MCP-BE 호출 관련 설정(기존 키 유지) */
    @Value("${logging.callback-url:ws://localhost:18080/ws/log}")
    private String callbackUrl;

    @Value("${logging.mcp-register-path:/mcp/logging/register}")
    private String registerPath;

    @Value("${logging.mcp-unregister-path:/mcp/logging/unregister}") // 해제 경로(키는 path variable)
    private String unregisterPath;

    @Value("${logging.mcp-timeout-ms:5000}")
    private long timeoutMs;

    /* ===================== 공개 API ===================== */

    /** BACKEND 소스 등록 */
    public void registerBackend(BackendLogSourceRegisterDto dto) {
        McpLogRegisterRequestDto req = LogRegisterMapper.toMcpFromBack(dto, callbackUrl);
        callMcpRegister(req, "MCP register[BACKEND] " + dto.ec2Url());
    }

    /** FRONTEND 소스 등록 */
    public void registerFrontend(FrontendLogSourceRegisterDto dto) {
        McpLogRegisterRequestDto req = LogRegisterMapper.toMcpFromFront(dto, callbackUrl);
        // ⚠️ DTO 스펙(필드명/메서드명)이 환경마다 다를 수 있어 로그 컨텍스트에서 직접 참조하지 않는다.
        callMcpRegister(req, "MCP register[FRONTEND]");
    }

    /** RDS 소스 등록 */
    public void registerRds(RdsLogSourceRegisterDto dto) {
        boolean hasNatural =
                (dto.rdsInstanceId() != null && !dto.rdsInstanceId().isBlank()) ||
                        (dto.dbAddress() != null && !dto.dbAddress().isBlank());
        if (!hasNatural) throw new CustomException(ErrorCode.RDS_KEY_REQUIRED);

        McpLogRegisterRequestDto req = LogRegisterMapper.toMcpFromDb(dto, callbackUrl);
        String natural = (dto.rdsInstanceId()!=null && !dto.rdsInstanceId().isBlank())
                ? dto.rdsInstanceId() : dto.dbAddress();
        callMcpRegister(req, "MCP register[RDS] " + natural);
    }

    /** 파이프라인 해제 (internalKey 기반) */
    public void unregister(String internalKey) {
        callMcpUnregister(internalKey, "MCP unregister " + internalKey);
    }

    /* ===================== 내부 공통 호출부 ===================== */

    /**
     * 등록 호출(POST /mcp/logging/register)
     * - DTO → JSON 바이트로 직렬화
     * - 그 바이트로 HMAC 계산 → X-Signature 헤더 첨부
     */
    private void callMcpRegister(McpLogRegisterRequestDto req, String ctx) {
        try {
            byte[] body = objectMapper.writeValueAsBytes(req); // 전송 바이트 그대로 사용
            String sig = isBlank(sharedSecret) ? null : hmacSha256Hex(sharedSecret, body);

            var spec = webClient.post()
                    .uri(registerPath)
                    .contentType(MediaType.APPLICATION_JSON);
            if (sig != null) spec = spec.header("X-Signature", sig);

            spec.bodyValue(body)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), resp -> toMcpRegisterFail(resp, ctx))
                    .onStatus(status -> status.is5xxServerError(), resp -> toMcpConnectFail(resp, ctx))
                    .toBodilessEntity()
                    .timeout(Duration.ofMillis(timeoutMs))
                    // ✅ 메서드 레퍼런스 → 람다로 변경, 컨텍스트 함께 전달
                    .onErrorMap(ex -> translator.translate(ex, ctx))
                    .block();

            log.info("✅ {} -> OK", ctx);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.MCP_CONNECT_FAIL);
        }
    }

    /**
     * 해제 호출(DELETE /mcp/logging/unregister/{internalKey})
     * - 바디 없음(0바이트) → 0바이트에 대한 HMAC 서명 값을 X-Signature로 첨부
     */
    private void callMcpUnregister(String internalKey, String ctx) {
        try {
            byte[] empty = new byte[0];
            String sig = isBlank(sharedSecret) ? null : hmacSha256Hex(sharedSecret, empty);

            var spec = webClient.delete()
                    .uri(unregisterPath + "/{internalKey}", internalKey);
            if (sig != null) spec = spec.header("X-Signature", sig);

            spec.retrieve()
                    .onStatus(status -> status.is4xxClientError(), resp -> toMcpRegisterFail(resp, ctx))
                    .onStatus(status -> status.is5xxServerError(), resp -> toMcpConnectFail(resp, ctx))
                    .toBodilessEntity()
                    .timeout(Duration.ofMillis(timeoutMs))
                    .onErrorMap(ex -> translator.translate(ex, ctx))
                    .block();

            log.info("✅ {} -> OK", ctx);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.MCP_CONNECT_FAIL);
        }
    }

    /** 4xx → 고객/요청 문제 */
    private Mono<? extends Throwable> toMcpRegisterFail(ClientResponse resp, String ctx) {
        return resp.bodyToMono(String.class).defaultIfEmpty("")
                .map(body -> {
                    log.error("❌ {} -> 4xx, body={}", ctx, body);
                    return new CustomException(ErrorCode.MCP_REGISTER_FAIL);
                });
    }

    /** 5xx → 상대 서비스 장애/연결 문제 */
    private Mono<? extends Throwable> toMcpConnectFail(ClientResponse resp, String ctx) {
        return resp.bodyToMono(String.class).defaultIfEmpty("")
                .map(body -> {
                    log.error("❌ {} -> 5xx, body={}", ctx, body);
                    return new CustomException(ErrorCode.MCP_CONNECT_FAIL);
                });
    }

    /* ===================== 내부 HMAC 유틸 (새 파일 없이) ===================== */

    /**
     * HMAC-SHA256(body) → hex 문자열
     * - 비밀키는 ENV/시크릿으로 주입(@Value: logging.shared-secret).
     * - 바디는 실제 전송될 바이트 그대로 넣어야 한다.
     */
    private static String hmacSha256Hex(String secret, byte[] body) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(java.nio.charset.StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] out = mac.doFinal(body);
            StringBuilder sb = new StringBuilder(out.length * 2);
            for (byte b : out) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("HMAC failure", e);
        }
    }

    /* ===================== helpers ===================== */
    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}

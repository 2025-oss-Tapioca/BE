package com.tapioca.BE.adapter.out.httpImpl;

import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * [외부 HTTP 호출(WebClient) 예외 → 프로젝트 표준 예외 변환기]
 * 위치: adapter.out.httpImpl  (HTTP 기반 아웃바운드 어댑터 전용 유틸)
 *
 * 매핑 규칙 (408xx: MCP/RDS 도메인)
 *  - WebClientResponseException (4xx/5xx 응답) → MCP_REGISTER_FAIL(40800)
 *  - WebClientRequestException  (DNS/연결/소켓/타임아웃) → MCP_CONNECT_FAIL(40801)
 *  - 그 외 알 수 없는 예외 → SERVER_ERROR(50000)
 *
 * 이 컴포넌트는 인프라 세부사항을 application/domain에 새게 하지 않기 위해
 * adapter.out 레이어에서만 사용한다.
 */
@Slf4j
@Component
public class WebClientErrorTranslator {

    /**
     * WebClient 예외를 CustomException 으로 변환한다.
     * @param ex      원본 예외
     * @param context 로깅용 컨텍스트(어떤 호출인지 식별: ex) "MCP register[BACKEND] ..."
     * @return 변환된 CustomException (408xx/50000)
     */
    public CustomException translate(Throwable ex, String context) {
        // 1) MCP가 4xx/5xx 상태코드로 응답한 경우 (응답은 왔지만 에러)
        if (ex instanceof WebClientResponseException e) {
            log.error("[HTTP {}] {}  body={}", e.getRawStatusCode(), context, safeBody(e));
            return new CustomException(ErrorCode.MCP_REGISTER_FAIL); // 40800
        }

        // 2) 네트워크 계층 자체 실패 (연결 실패, 소켓 타임아웃 등)
        if (ex instanceof WebClientRequestException e) {
            log.error("[HTTP CONNECT FAIL] {}  cause={}", context, e.getMessage());
            return new CustomException(ErrorCode.MCP_CONNECT_FAIL); // 40801
        }

        // 3) 그 외 예기치 못한 오류
        log.error("[HTTP UNKNOWN] {}  ex={}", context, ex.toString(), ex);
        return new CustomException(ErrorCode.SERVER_ERROR);
    }

    private String safeBody(WebClientResponseException e) {
        try { return e.getResponseBodyAsString(); }
        catch (Exception ignore) { return "<no-body>"; }
    }
}

package com.tapioca.BE.adapter.out.httpImpl;

import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * WebClient 호출 예외 → 프로젝트 표준 예외(CustomException) 변환기
 */
@Slf4j
@Component
public class WebClientErrorTranslator {

    /**
     * WebClient 예외를 CustomException으로 변환
     */
    public CustomException translate(Throwable ex, String context) {

        // [케이스 1] 서버 응답은 받았지만 상태코드가 4xx 또는 5xx인 경우
        if (ex instanceof WebClientResponseException e) {
            log.error("[HTTP {}] {}  body={}", e.getRawStatusCode(), context, safeBody(e));
            return new CustomException(ErrorCode.MCP_REGISTER_FAIL); // 40800
        }

        // [케이스 2] 서버 응답조차 받지 못한 네트워크 계층 실패
        if (ex instanceof WebClientRequestException e) {
            log.error("[HTTP CONNECT FAIL] {}  cause={}", context, e.getMessage());
            return new CustomException(ErrorCode.MCP_CONNECT_FAIL); // 40801
        }

        // [케이스 3] 예상하지 못한 예외
        log.error("[HTTP UNKNOWN] {}  ex={}", context, ex.toString(), ex);
        return new CustomException(ErrorCode.SERVER_ERROR);
    }

    /**
     * 응답 본문을 안전하게 문자열로 변환
     */
    private String safeBody(WebClientResponseException e) {
        try {
            return e.getResponseBodyAsString();
        } catch (Exception ignore) {
            return "<no-body>";
        }
    }
}
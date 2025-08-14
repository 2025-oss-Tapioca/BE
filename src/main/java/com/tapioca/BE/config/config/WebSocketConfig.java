package com.tapioca.BE.config.config;

import com.tapioca.BE.adapter.in.websocket.LogWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket 전역 설정 클래스.
 *
 * 역할
 * ─────────────────────────────────────────────
 * - WebSocket 엔드포인트(/ws/log) 등록
 * - LogWebSocketHandler를 해당 경로에 매핑하여
 *   MCP-BE ↔ BE ↔ 클라이언트 간 실시간 로그 송수신 가능하게 함
 *
 * 설계 포인트
 * ─────────────────────────────────────────────
 * 1) @EnableWebSocket:
 *    - Spring MVC 기반 WebSocket 기능 활성화
 *    - STOMP 사용이 아닌, 저수준(TextWebSocketHandler) 방식 선택
 *
 * 2) /ws/log 엔드포인트:
 *    - MCP 서버와 브라우저 클라이언트 모두 이 경로로 접속
 *    - 메시지 내부 JSON의 "type" 필드로 register/log/filter 구분
 *
 * 3) CORS 정책:
 *    - 현재는 setAllowedOrigins("*")로 모든 도메인 허용 (개발 편의용)
 *    - 운영 환경에서는 보안상 특정 도메인만 허용하도록 제한 권장
 *
 * 4) 확장 가능성:
 *    - addInterceptors()를 통해 인증/로그인 여부 검사 가능
 *    - withSockJS()로 폴백 지원 가능 (SockJS 클라이언트 사용 시)
 */
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    /** 실시간 로그 수집 및 브로드캐스트 로직이 구현된 WebSocket 핸들러 */
    private final LogWebSocketHandler logWebSocketHandler;

    /**
     * WebSocket 핸들러를 경로(/ws/log)에 등록.
     * MCP-BE 및 프론트엔드 클라이언트는 이 경로로 접속해
     * JSON 기반 메시지를 송수신한다.
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(logWebSocketHandler, "/ws/log")
                // 운영 환경에서는 허용 도메인 화이트리스트 적용 필요
                .setAllowedOrigins("*");
    }
}

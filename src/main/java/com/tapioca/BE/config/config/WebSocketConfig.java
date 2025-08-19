package com.tapioca.BE.config.config;

import com.tapioca.BE.adapter.in.websocket.LogWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket 전역 설정 클래스
 * - WebSocket 엔드포인트(/ws/log) 등록
 *    - 서버가 HTTPS를 사용하면 WebSocket 연결도 자동으로 WSS 사용
 *    - 프록시(Nginx, AWS ALB 등) 사용 시 WebSocket 업그레이드 허용 필요
 */
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    /** 실시간 로그 수집 및 브로드캐스트 로직이 구현된 WebSocket 핸들러 */
    private final LogWebSocketHandler logWebSocketHandler;

    /**
     * WebSocket 핸들러를 경로(/ws/log)에 등록
     * MCP-BE 및 프론트엔드 클라이언트는 이 경로로 접속해 JSON 기반 메시지를 송수신한다.
     *   - 제한 필요 시 허용할 Origin을 명시적으로 설정 가능
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(logWebSocketHandler, "/ws/log")
                .setAllowedOrigins("*"); // 모든 Origin 허용
    }
}
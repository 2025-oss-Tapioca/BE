package com.tapioca.BE.adapter.in.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;


/**
 * WebSocket 연결 관리 및 메시지 위임 처리 핸들러
 * - 연결/해제/에러 이벤트에서 세션을 등록·제거
 * - 수신 메시지를 별도의 메시지 핸들러로 위임 처리
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LogWebSocketHandler extends TextWebSocketHandler { // 텍스트 메시지용 기본 핸들러 확장

    private final LogWebSocketMessageHandler messageHandler;    // 메시지 파싱/유즈케이스 호출 담당
    private final LogWebSocketSessionManager sessionManager;    // 세션/그룹 관리 담당

    @Override
    public void afterConnectionEstablished(WebSocketSession session) { // 클라이언트가 연결되었을 때
        sessionManager.registerSession(session);                        // 세션 저장
        log.info("[WS] connected: {}", safeId(session));                // 접속 로그
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) { // 텍스트 메시지 수신
        messageHandler.handle(session, message.getPayload());                           // 메시지 본문 위임 처리
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {   // 전송 에러
        log.warn("[WS] transport error (session={}): {}", safeId(session), exception.toString());
        sessionManager.unregisterSession(session);                                       // 에러 시 세션 정리
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {   // 연결 종료
        log.info("[WS] closed: {} ({})", safeId(session), status);                      // 종료 로그
        sessionManager.unregisterSession(session);                                       // 세션 정리
    }

    private String safeId(WebSocketSession s) {                                         // NPE 방지용 세션 ID 헬퍼
        try { return (s != null ? s.getId() : "null"); } catch (Exception ignore) { return "unknown"; }
    }
}
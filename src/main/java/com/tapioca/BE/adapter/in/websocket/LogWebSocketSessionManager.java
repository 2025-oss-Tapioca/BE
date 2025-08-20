package com.tapioca.BE.adapter.in.websocket;

import com.tapioca.BE.domain.model.log.SourceKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 세션과 SourceKey(로그 소스)를 매핑하고,
 * 그룹별(WebSocket 연결 집합별) 메시지 전송을 관리하는 클래스
 */
@Slf4j
@Component
public class LogWebSocketSessionManager {

    // 모든 연결된 세션을 저장 (sessionId → WebSocketSession)
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    // 세션이 어떤 SourceKey에 바인딩되어 있는지 저장 (sessionId → SourceKey)
    private final Map<String, SourceKey> bindings = new ConcurrentHashMap<>();

    // SourceKey 별로 어떤 세션들이 연결되어 있는지 저장 (SourceKey → sessionId 집합)
    private final Map<SourceKey, Set<String>> groups = new ConcurrentHashMap<>();

    // 새로운 세션 등록
    public void registerSession(WebSocketSession s) {
        sessions.put(s.getId(), s);
    }

    // 세션 해제 및 모든 매핑 제거
    public void unregisterSession(WebSocketSession s) {
        if (s == null) return;
        sessions.remove(s.getId());                    // 세션 목록에서 제거
        SourceKey key = bindings.remove(s.getId());    // 바인딩 정보 제거
        if (key != null) {
            Set<String> set = groups.get(key);
            if (set != null) set.remove(s.getId());    // 그룹에서 제거
        }
        try {
            if (s.isOpen()) s.close();                 // 소켓 닫기
        } catch (Exception ignore) {}
    }

    // 세션을 특정 SourceKey에 바인딩
    public void bindSession(WebSocketSession s, SourceKey key) {
        bindings.put(s.getId(), key);                                             // 바인딩 정보 저장
        groups.computeIfAbsent(key, k -> new CopyOnWriteArraySet<>())             // 해당 그룹 없으면 생성
                .add(s.getId());                                                    // 그룹에 세션 추가
    }

    // 세션이 바인딩된 SourceKey 반환
    public SourceKey boundKey(WebSocketSession s) {
        return bindings.get(s.getId());
    }

    // 특정 SourceKey 그룹에 속한 모든 세션에 메시지 전송
    public void broadcastTo(SourceKey key, String jsonPayload) {
        Set<String> ids = groups.getOrDefault(key, Set.of());
        for (String sid : ids) {
            WebSocketSession target = sessions.get(sid);
            if (target == null || !target.isOpen()) continue;                     // 연결이 끊긴 세션 건너뜀
            try {
                target.sendMessage(new TextMessage(jsonPayload));                 // 메시지 전송
            } catch (Exception e) {
                log.warn("[WS] send fail -> remove session: {}", sid);             // 전송 실패 시 로그
                unregisterSession(target);                                        // 세션 해제
            }
        }
    }

    // 특정 세션에만 메시지 전송
    public void sendText(WebSocketSession s, String rawJson) {
        try {
            s.sendMessage(new TextMessage(rawJson));
        } catch (Exception e) {
            unregisterSession(s); // 전송 실패 시 세션 해제
        }
    }
}
package com.tapioca.BE.adapter.out.websocketImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket 브로드캐스터.
 *
 * 설계 메모
 * - CopyOnWriteArraySet: "세션 추가/제거는 가끔, 브로드캐스트는 자주".
 *   (쓰기 비용이 비싸지만 읽기/순회는 락 없이 안전)
 * - 브로드캐스트 메시지는 이미 JSON 문자열이라고 가정(핸들러에서 DTO→JSON으로 변환).
 * - 민감정보/대용량 로그를 INFO로 남기지 않음. (미리보기만 DEBUG로 출력)
 */
@Slf4j
@Component
public class LogBroadCasterImpl {

    /** 연결된 WS 세션 목록(스레드 안전) */
    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    /** 브로드캐스트 로그 미리보기 최대 길이(로그 노이즈/민감정보 노출 방지) */
    private static final int PREVIEW_LIMIT = 200;

    /** 클라이언트 세션 추가 (핸들러의 afterConnectionEstablished에서 호출) */
    public void addSession(WebSocketSession session) {
        if (session != null) {
            sessions.add(session);
            log.info("WS session added: {} (active={})", session.getId(), sessions.size());
        }
    }

    /** 클라이언트 세션 제거 (핸들러의 afterConnectionClosed에서 호출) */
    public void removeSession(WebSocketSession session) {
        if (session != null) {
            sessions.remove(session);
            log.info("WS session removed: {} (active={})", session.getId(), sessions.size());
        }
    }

    /**
     * 모든 세션으로 JSON 문자열을 브로드캐스트.
     * @param json 이미 직렬화된 JSON 문자열(Null/빈 문자열은 무시)
     */
    public void broadcast(String json) {
        if (json == null || json.isBlank()) {
            // 빈 메시지는 전송하지 않음(불필요한 프레임 방지)
            log.debug("Skip broadcast: empty message");
            return;
        }

        // 로그 노이즈/민감정보 리스크 줄이기 위해 preview만 남김
        if (log.isDebugEnabled()) {
            log.debug("Broadcast preview: {}", preview(json, PREVIEW_LIMIT));
        }

        for (WebSocketSession session : sessions) {
            if (session == null) continue;

            if (!session.isOpen()) {
                // 닫힌 세션은 즉시 정리
                sessions.remove(session);
                log.warn("Removed closed session: {} (active={})", safeId(session), sessions.size());
                continue;
            }

            try {
                session.sendMessage(new TextMessage(json));
            } catch (IOException e) {
                // 전송 실패: 세션 정리 + 경고 로그
                log.warn("Failed to send WS message. Remove session: {} - {}", safeId(session), e.getMessage());
                safeClose(session);
                sessions.remove(session);
            } catch (Exception e) {
                // 기타 예외: 세션 제거하고 오류 로그
                log.error("Unexpected WS error. Remove session: {}", safeId(session), e);
                safeClose(session);
                sessions.remove(session);
            }
        }
    }

    /** 현재 활성 세션 수 (모니터링/디버깅 용도) */
    public int getActiveSessionCount() {
        return sessions.size();
    }

    // ----------------- helpers -----------------

    /** 로그용 세션 ID 안전 추출 */
    private String safeId(WebSocketSession s) {
        try { return (s != null ? s.getId() : "null"); }
        catch (Exception ignore) { return "unknown"; }
    }

    /** 세션 안전 종료(예외 무시) */
    private void safeClose(WebSocketSession s) {
        try { if (s != null && s.isOpen()) s.close(); }
        catch (Exception ignore) { /* no-op */ }
    }

    /** 문자열 미리보기(길이 제한) */
    private String preview(String s, int limit) {
        if (s == null) return "null";
        if (s.length() <= limit) return s;
        return s.substring(0, limit) + "...(+" + (s.length() - limit) + " chars)";
    }
}

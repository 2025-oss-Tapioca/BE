package com.tapioca.BE.domain.model.log;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * [도메인: 로그 엔트리]
 * - 외부에서 수신한 Raw 로그(JSON 등)를 내부 표준 형태로 변환하여 보관하는 불변(immutable) 객체
 * - WebSocket/HTTP 수신 시 → Map → LogMapper → LogEntry 형태로 변환
 */
public final class LogEntry {

    /** toString()에서 표시할 메시지 최대 길이 (넘으면 잘라서 표시) */
    private static final int PREVIEW_LIMIT = 200;

    /** 로그 발생 시각 (필수, null 불가) */
    private final LocalDateTime timestamp;

    /** 로그 레벨 (예: INFO, WARN, ERROR) */
    private final String level;

    /** 서비스/모듈 이름 */
    private final String service;

    /** 로그 메시지 본문 */
    private final String message;

    /** 생성자 */
    public LogEntry(LocalDateTime timestamp, String level, String service, String message) {
        if (timestamp == null) {
            throw new IllegalArgumentException("timestamp must not be null");
        }
        this.timestamp = timestamp;
        this.level = level;
        this.service = service;
        this.message = message;
    }

    // ───── Getter (불변 객체라 Setter 없음) ─────
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getLevel() { return level; }
    public String getService() { return service; }
    public String getMessage() { return message; }

    // ───── equals / hashCode (버퍼 검색 및 중복 비교에 활용 가능) ─────
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogEntry that)) return false;
        return Objects.equals(timestamp, that.timestamp)
                && Objects.equals(level, that.level)
                && Objects.equals(service, that.service)
                && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, level, service, message);
    }

    // ───── 로깅 친화적 toString (본문은 PREVIEW_LIMIT까지만) ─────
    @Override
    public String toString() {
        String msgPreview = (message == null)
                ? "null"
                : (message.length() <= PREVIEW_LIMIT
                ? message
                : message.substring(0, PREVIEW_LIMIT) + "...(+" + (message.length() - PREVIEW_LIMIT) + " chars)");
        return "[" + timestamp + "] [" + level + "] [" + service + "] " + msgPreview;
    }
}
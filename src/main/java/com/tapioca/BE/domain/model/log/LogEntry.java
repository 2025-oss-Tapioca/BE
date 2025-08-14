package com.tapioca.BE.domain.model.log;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * [도메인: 로그 엔트리]
 * - 외부(raw) → LogMapper → LogEntry 로 매핑되어 메모리 버퍼에 저장되는 "내부 표준 모델"
 * - 불변(immutable) 설계: 필드 변경 불가 → 동시성/방어적 프로그래밍에 유리
 *
 * 필드
 *  - timestamp : 로그 발생 시각 (필수)
 *  - level     : 로그 레벨 (예: INFO/WARN/ERROR…) — 대문자 권장(매퍼에서 정규화)
 *  - service   : 서비스/모듈 식별자 — null/blank면 "-" 사용 권장(매퍼에서 처리)
 *  - message   : 로그 본문 — null 허용(매퍼에서 trim 처리)
 *
 * 주의
 *  - JSON 역직렬화는 이 도메인에서 수행하지 않는다. (WS 수신 → Map → LogMapper → LogEntry)
 *  - toString은 운영 로그 노이즈/민감정보를 줄이기 위해 미리보기 형태로만 출력한다.
 */
public final class LogEntry {

    private static final int PREVIEW_LIMIT = 200; // toString 미리보기 길이

    private final LocalDateTime timestamp; // 로그 발생 시각(필수)
    private final String level;            // 로그 레벨
    private final String service;          // 서비스/모듈명
    private final String message;          // 본문

    /**
     * 모든 필드를 초기화하는 생성자.
     * - timestamp는 null 불가 (필수)
     * - level/service/message는 null 허용(매퍼에서 기본값/정규화 가정)
     */
    public LogEntry(LocalDateTime timestamp, String level, String service, String message) {
        if (timestamp == null) {
            throw new IllegalArgumentException("timestamp must not be null");
        }
        this.timestamp = timestamp;
        this.level = level;
        this.service = service;
        this.message = message;
    }

    // ----- Getters (불변) -----
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getLevel() { return level; }
    public String getService() { return service; }
    public String getMessage() { return message; }

    // ----- 동등성/해시 -----
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

    // ----- 로깅 친화적 toString (미리보기) -----
    @Override
    public String toString() {
        String msg = (message == null) ? "null"
                : (message.length() <= PREVIEW_LIMIT
                ? message
                : message.substring(0, PREVIEW_LIMIT) + "...(+" + (message.length() - PREVIEW_LIMIT) + " chars)");
        return "[" + timestamp + "] [" + level + "] [" + service + "] " + msg;
    }
}

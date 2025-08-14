package com.tapioca.BE.adapter.out.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tapioca.BE.application.dto.response.log.LogResponseDto;
import com.tapioca.BE.domain.model.log.LogEntry;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

/**
 * 로그 스트림 전용 매퍼 (등록 매퍼 아님!)
 *
 * 역할
 * ─────────────────────────────────────────────────────────────────────────────
 * 1) Raw(Map)  → Domain(LogEntry)
 *    - MCP-BE/에이전트가 WS로 밀어주는 "원시 로그 맵"을 도메인 객체로 변환
 *    - 외부 입력이므로 형/널/포맷을 방어적으로 파싱
 *
 * 2) Domain(LogEntry) → DTO(LogResponseDto)
 *    - 프론트로 브로드캐스트/응답할 "표준 로그 DTO"로 변환
 *    - 날짜는 ISO-8601 문자열로 통일, 레벨은 대문자 권장, 서비스명 기본값 처리
 *
 * 주의
 * ─────────────────────────────────────────────────────────────────────────────
 * - 이 클래스는 "런타임 로그 스트림"을 위한 변환기다.
 *   등록 경로(REST)에서 쓰는 매퍼(예: LogRegisterMapper)와 혼동하지 말 것.
 * - timestamp는 다양한 표현을 허용한다:
 *   (1) ISO_LOCAL_DATE_TIME  예: "2025-08-12T10:15:30"
 *   (2) ISO_OFFSET_DATE_TIME 예: "2025-08-12T10:15:30Z", "2025-08-12T10:15:30+09:00"
 *   (3) ISO_ZONED_DATE_TIME  예: "2025-08-12T10:15:30+09:00[Asia/Seoul]"
 *   (4) epoch millis/seconds  예: 1723437330000, 1723437330
 * - 내부 표준 시간은 LocalDateTime(존재 타임존 無)로 통일:
 *   Offset/Zoned/epoch 입력은 UTC 기준으로 환산한 후 LocalDateTime으로 유지.
 */
public final class LogMapper {

    /** ISO-8601(LocalDateTime) 출력용 포맷터 (예: "2025-08-12T10:15:30") */
    private static final DateTimeFormatter ISO_LOCAL = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private LogMapper() {
        // 유틸 클래스: 인스턴스화 금지
    }

    // =========================================================================
    // Domain(LogEntry) → DTO(LogResponseDto)
    // =========================================================================

    /**
     * 도메인(LogEntry)을 프론트로 내보낼 표준 응답 DTO로 변환한다.
     *
     * 변환 규칙
     * - timestamp: LocalDateTime → ISO-8601 문자열
     * - level    : 대문자(표준화) (null 허용)
     * - service  : null/공백이면 "-" 기본값
     * - message  : trim 적용(null 허용)
     *
     * @param entry 로그 도메인 객체 (null 허용)
     * @return 표준 로그 응답 DTO
     */
    public static LogResponseDto toResponseDto(LogEntry entry) {
        if (entry == null) {
            // NPE 방지: null-safe하게 비어있는 DTO 전달
            return new LogResponseDto(null, null, null, null);
        }
        String ts = (entry.getTimestamp() != null) ? entry.getTimestamp().format(ISO_LOCAL) : null;
        return new LogResponseDto(
                ts,
                safeUpper(entry.getLevel()),
                defaultIfBlank(entry.getService(), "-"),
                safeTrim(entry.getMessage())
        );
    }

    // =========================================================================
    // Raw(Map) → Domain(LogEntry)
    // =========================================================================

    /**
     * 원시 로그 맵(rawData)을 도메인(LogEntry)으로 변환한다.
     *
     * 기대 입력 키 (대소문자 구분함)
     * - "timestamp": String(ISO-8601) 또는 Number(epoch millis/seconds)
     * - "level"    : String (null 가능)
     * - "service"  : String (null/공백이면 "-" 기본값 적용)
     * - "message"  : String (null 가능)
     *
     * 방어 로직
     * - rawData == null → IllegalArgumentException
     * - timestamp 누락/형식 오류 → IllegalArgumentException
     * - level/service/message는 타입/널 안전 처리 및 후처리(trim/upper/default)
     *
     * @param rawData       외부(수집기/에이전트)로부터 전달된 원시 로그 맵
     * @param objectMapper  현재 구현에서는 사용하지 않음(시그니처 호환 목적)
     * @return              도메인 로그 엔트리
     */
    @SuppressWarnings("unused")
    public static LogEntry toDomainFromRawData(Map<String, Object> rawData, ObjectMapper objectMapper) {
        if (rawData == null) {
            throw new IllegalArgumentException("rawData must not be null");
        }

        // ── timestamp 파싱 (필수)
        LocalDateTime ts = parseTimestamp(rawData.get("timestamp"));
        if (ts == null) {
            throw new IllegalArgumentException("timestamp is required and must be ISO-8601 string or epoch number");
        }

        // ── 나머지 필드 파싱 (선택 + 후처리)
        String level   = safeUpper(asString(rawData.get("level")));
        String service = defaultIfBlank(asString(rawData.get("service")), "-");
        String message = safeTrim(asString(rawData.get("message")));

        return new LogEntry(ts, level, service, message);
    }

    // =========================================================================
    // Helpers (파싱/후처리 유틸)
    // =========================================================================

    /**
     * 다양한 timestamp 표현을 LocalDateTime(UTC 기준 환산)으로 파싱한다.
     *
     * 허용 타입
     * - String:
     *     * ISO_LOCAL_DATE_TIME        → 그대로 LocalDateTime
     *     * ISO_OFFSET_DATE_TIME/ZONED → UTC로 변환 후 LocalDateTime
     * - Number:
     *     * 13자리 이상 → millis 로 간주
     *     * 그 외       → seconds 로 간주
     *
     * 실패 시 null 반환(호출부에서 필수 처리)
     */
    private static LocalDateTime parseTimestamp(Object v) {
        if (v == null) return null;

        // 1) 문자열 케이스
        if (v instanceof String s) {
            String str = s.trim();
            if (str.isEmpty()) return null;

            // 1-1) ISO_LOCAL_DATE_TIME: "2025-08-12T10:15:30"
            try {
                return LocalDateTime.parse(str, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (DateTimeParseException ignore) { /* 다음 포맷 시도 */ }

            // 1-2) ISO_OFFSET_DATE_TIME: "2025-08-12T10:15:30Z" / "+09:00"
            try {
                OffsetDateTime odt = OffsetDateTime.parse(str, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                return odt.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            } catch (DateTimeParseException ignore) { /* 다음 포맷 시도 */ }

            // 1-3) ISO_ZONED_DATE_TIME: "…+09:00[Asia/Seoul]"
            try {
                ZonedDateTime zdt = ZonedDateTime.parse(str, DateTimeFormatter.ISO_ZONED_DATE_TIME);
                return zdt.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            } catch (DateTimeParseException ignore) {
                return null; // 문자열인데 모두 실패 → null
            }
        }

        // 2) 숫자 케이스: epoch millis/seconds
        if (v instanceof Number n) {
            long epoch = n.longValue();
            Instant instant = (epoch >= 1_000_000_000_000L) // 13자리 이상이면 millis로 판단
                    ? Instant.ofEpochMilli(epoch)
                    : Instant.ofEpochSecond(epoch);
            return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        }

        // 지원하지 않는 타입
        return null;
    }

    /** Object → String 안전 변환 (null이면 null, 문자열 아닌 경우 toString) */
    private static String asString(Object v) {
        if (v == null) return null;
        return (v instanceof String s) ? s : String.valueOf(v);
    }

    /** 문자열 trim (null-safe) */
    private static String safeTrim(String s) {
        return (s == null) ? null : s.trim();
    }

    /** 공백/널이면 기본값으로 대체 */
    private static String defaultIfBlank(String s, String def) {
        return (s == null || s.trim().isEmpty()) ? def : s.trim();
    }

    /** 대문자 정규화(null-safe) */
    private static String safeUpper(String s) {
        return (s == null) ? null : s.toUpperCase();
    }
}

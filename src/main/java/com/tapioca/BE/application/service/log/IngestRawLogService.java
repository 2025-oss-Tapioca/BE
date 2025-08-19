package com.tapioca.BE.application.service.log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tapioca.BE.domain.model.log.LogEntry;
import com.tapioca.BE.domain.model.log.SourceKey;
import com.tapioca.BE.domain.port.in.usecase.log.IngestRawLogUseCase;
import com.tapioca.BE.domain.port.in.usecase.log.PushLogUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IngestRawLogService implements IngestRawLogUseCase {

    private final ObjectMapper objectMapper;
    private final PushLogUseCase pushLogUseCase;

    @Override
    public LogEntry ingest(SourceKey key, Map<String, Object> rawData) {
        if (key == null || rawData == null) {
            throw new IllegalArgumentException("key/rawData must not be null");
        }

        // Map -> JsonNode (유연한 타입 처리)
        final JsonNode root = objectMapper.valueToTree(rawData);

        // 필드 추출
        Object tsRaw = extractTimestampRaw(root.get("timestamp"));
        String level = textOrNull(root.get("level"));
        String service = textOrNull(root.get("service"));
        String message = textOrNull(root.get("message"));

        // 타임스탬프 파싱 (ISO/Offset/Zoned/epoch + 분까지만)
        LocalDateTime ts = parseTimestamp(tsRaw);
        if (ts == null) {
            throw new IllegalArgumentException("timestamp is required and must be ISO/epoch/supported pattern");
        }

        LogEntry entry = new LogEntry(
                ts,
                toUpper(level),
                defaultIfBlank(service, "-"),
                trimOrNull(message)
        );

        // 버퍼 적재
        pushLogUseCase.push(key, entry);

        // 핸들러가 브로드캐스트에 사용
        return entry;
    }

    /* ======================= helpers ======================= */

    private static Object extractTimestampRaw(JsonNode node) {
        if (node == null || node.isNull()) return null;
        if (node.isNumber()) return node.numberValue();
        // 숫자 문자열("1723872000", "1723872000000")도 허용
        if (node.isTextual()) {
            String s = node.asText();
            // 숫자만으로 구성되어 있으면 Long으로 시도
            if (s.matches("^-?\\d+$")) {
                try { return Long.parseLong(s); } catch (NumberFormatException ignore) { /* fallthrough */ }
            }
            return s;
        }
        // 객체/배열 등은 미지원
        return node.asText(null);
    }

    private static String textOrNull(JsonNode n) {
        return (n == null || n.isNull()) ? null : n.asText(null);
    }

    private static String trimOrNull(String s) { return (s == null) ? null : s.trim(); }
    private static String defaultIfBlank(String s, String def) { return (s == null || s.trim().isEmpty()) ? def : s.trim(); }
    private static String toUpper(String s) { return (s == null) ? null : s.toUpperCase(Locale.ROOT); }

    /* =================== timestamp parsing =================== */

    private static final DateTimeFormatter[] LOCAL_DT_PATTERNS = new DateTimeFormatter[] {
            // 초/밀리초
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS", Locale.US),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS", Locale.US),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US),
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss", Locale.US),
            DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss", Locale.US),
            // 분까지만 (초는 00으로 보정)
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.US),
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm", Locale.US),
            DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm", Locale.US),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", Locale.US)
    };

    /**
     * 다양한 timestamp 표현을 LocalDateTime(UTC 기준)으로 정규화
     */
    private static LocalDateTime parseTimestamp(Object v) {
        if (v == null) return null;

        if (v instanceof String s) {
            String str = s.trim();
            if (str.isEmpty()) return null;

            // ISO_LOCAL_DATE_TIME
            try { return LocalDateTime.parse(str, DateTimeFormatter.ISO_LOCAL_DATE_TIME); }
            catch (DateTimeParseException ignore) {}

            // ISO_OFFSET_DATE_TIME → UTC
            try {
                OffsetDateTime odt = OffsetDateTime.parse(str, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                return odt.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            } catch (DateTimeParseException ignore) {}

            // ISO_ZONED_DATE_TIME → UTC
            try {
                ZonedDateTime zdt = ZonedDateTime.parse(str, DateTimeFormatter.ISO_ZONED_DATE_TIME);
                return zdt.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            } catch (DateTimeParseException ignore) {}

            // 커스텀 로컬 패턴들
            for (DateTimeFormatter f : LOCAL_DT_PATTERNS) {
                try { return LocalDateTime.parse(str, f); }
                catch (DateTimeParseException ignore) {}
            }

            return null;
        }

        if (v instanceof Number n) {
            long epoch = n.longValue();
            Instant instant = (epoch >= 1_000_000_000_000L)
                    ? Instant.ofEpochMilli(epoch)   // millis
                    : Instant.ofEpochSecond(epoch); // seconds
            return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        }

        return null;
    }
}
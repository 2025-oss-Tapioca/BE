package com.tapioca.BE.adapter.in.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tapioca.BE.application.dto.response.log.LogResponseDto;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.config.exception.WsErrorMessage;
import com.tapioca.BE.domain.model.log.LogEntry;
import com.tapioca.BE.domain.model.log.SourceKey;
import com.tapioca.BE.domain.model.log.SourceType;
import com.tapioca.BE.domain.port.in.usecase.log.IngestRawLogUseCase;
import com.tapioca.BE.domain.port.in.usecase.log.QueryLogUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * WebSocket 메시지 처리기
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LogWebSocketMessageHandler {

    private final ObjectMapper objectMapper;                   // 직렬화
    private final LogWebSocketSessionManager sessions;         // 세션/그룹 전송
    private final IngestRawLogUseCase ingestRawLogUseCase;     // 파싱+적재 UC
    private final QueryLogUseCase queryLogUseCase;             // 조회 UC

    private static final int MAX_CONTEXT = 500;
    private static final DateTimeFormatter ISO_LOCAL = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @SuppressWarnings("unchecked")
    public void handle(WebSocketSession session, String payload) {
        try {
            Map<String, Object> msg = objectMapper.readValue(payload, Map.class);
            String type = strOrNull(msg.get("type"));
            if (type == null) throw new CustomException(ErrorCode.WS_UNKNOWN_TYPE);

            switch (type) {
                case "register"     -> handleRegister(session, msg);
                case "log"          -> handleLog(session, msg);
                case "filter"       -> handleFilter(session, msg);
                case "levelFilter"  -> handleLevelFilter(session, msg);
                default             -> { throw new CustomException(ErrorCode.WS_UNKNOWN_TYPE); }
            }

        } catch (CustomException ce) {
            sendWsError(session, ce.getErrorCode());
        } catch (Exception e) {
            log.error("[WS] handle error: {}", e.toString(), e);
            sendWsError(session, ErrorCode.WS_INTERNAL_ERROR);
        }
    }

    private void handleRegister(WebSocketSession session, Map<String, Object> msg) {
        String sourceTypeStr = strOrNull(msg.get("sourceType"));
        String teamCode      = strOrNull(msg.get("code")); // 팀코드만 받음

        if (isBlank(sourceTypeStr) || isBlank(teamCode)) {
            throw new CustomException(ErrorCode.WS_BAD_REGISTER);
        }

        try {
            SourceType st = SourceType.valueOf(sourceTypeStr.toUpperCase());
            SourceKey key = new SourceKey(st, teamCode);

            sessions.bindSession(session, key);
            sessions.sendText(session, "{\"type\":\"ack\",\"action\":\"register\",\"status\":\"ok\"}");
            log.info("[WS] registered: session={}, key={}", session.getId(), key);
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.WS_BAD_REGISTER);
        }
    }

    @SuppressWarnings("unchecked")
    private void handleLog(WebSocketSession session, Map<String, Object> msg) throws Exception {
        SourceKey key = sessions.boundKey(session);
        if (key == null) throw new CustomException(ErrorCode.WS_NOT_REGISTERED);

        Object dataObj = msg.get("data");
        if (!(dataObj instanceof Map<?, ?> raw)) {
            throw new CustomException(ErrorCode.WS_BAD_LOG_PAYLOAD);
        }

        // 파싱 + 버퍼 적재: 유즈케이스에 위임
        LogEntry entry = ingestRawLogUseCase.ingest(key, (Map<String, Object>) raw);

        // 어댑터는 DTO 변환 + 브로드캐스트만 수행
        LogResponseDto resp = toResponseDto(entry);
        sessions.broadcastTo(key, objectMapper.writeValueAsString(resp));
    }

    private void handleFilter(WebSocketSession session, Map<String, Object> msg) throws Exception {
        SourceKey key = sessions.boundKey(session);
        if (key == null) throw new CustomException(ErrorCode.WS_NOT_REGISTERED);

        String fromStr = strOrNull(msg.get("from"));
        String toStr   = strOrNull(msg.get("to"));
        if (isBlank(fromStr) || isBlank(toStr)) {
            throw new CustomException(ErrorCode.WS_BAD_FILTER_RANGE);
        }
        try {
            // 유연 파서: UTC LocalDateTime 정규화
            LocalDateTime from = parseFlexibleDateTime(fromStr);
            LocalDateTime to   = parseFlexibleDateTime(toStr);
            if (from.isAfter(to)) throw new CustomException(ErrorCode.WS_BAD_FILTER_RANGE);

            List<LogEntry> list = queryLogUseCase.getLogsBetween(key, from, to);
            for (LogEntry e : list) {
                sessions.sendText(session, objectMapper.writeValueAsString(toResponseDto(e)));
            }
            log.info("[WS] filter: key={}, from={}, to={}, size={}", key, from, to, list.size());

        } catch (DateTimeParseException ex) {
            throw new CustomException(ErrorCode.WS_INVALID_FILTER_DATE);
        }
    }

    private void handleLevelFilter(WebSocketSession session, Map<String, Object> msg) throws Exception {
        SourceKey key = sessions.boundKey(session);
        if (key == null) throw new CustomException(ErrorCode.WS_NOT_REGISTERED);

        String level = strOrNull(msg.get("level"));
        if (isBlank(level)) throw new CustomException(ErrorCode.WS_INVALID_LEVEL_FILTER);

        int requested = parseIntSafe(msg.get("context"), 0);
        int ctx = Math.max(0, Math.min(requested, MAX_CONTEXT));

        List<LogEntry> withCtx = queryLogUseCase.getContextLogsByLevel(key, level, ctx);
        for (LogEntry e : withCtx) {
            sessions.sendText(session, objectMapper.writeValueAsString(toResponseDto(e)));
        }
        log.info("[WS] levelFilter: key={}, level={}, ctx={}, size={}", key, level, ctx, withCtx.size());
    }

    /* ---------------- mapping ---------------- */

    private LogResponseDto toResponseDto(LogEntry entry) {
        if (entry == null) return new LogResponseDto(null, null, null, null);
        String ts = (entry.getTimestamp() != null) ? entry.getTimestamp().format(ISO_LOCAL) : null;
        String level = (entry.getLevel() == null) ? null : entry.getLevel().toUpperCase();
        String service = (entry.getService() == null || entry.getService().isBlank()) ? "-" : entry.getService().trim();
        String message = (entry.getMessage() == null) ? null : entry.getMessage().trim();
        return new LogResponseDto(ts, level, service, message);
    }

    /* ---------------- flexible datetime parsing ---------------- */

    // 공백/슬래시/점 구분 허용 + 초 생략 허용 패턴들 (로컬, 타임존 없는 경우)
    private static final DateTimeFormatter[] FLEX_LOCAL_PATTERNS = new DateTimeFormatter[] {
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
            // ISO 'T' 있으나 초 생략
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", Locale.US)
    };

    /**
     * 다양한 문자열 날짜를 UTC 기준 LocalDateTime으로 정규화
     * - epoch(sec/ms), ISO_LOCAL/ISO_OFFSET/ISO_ZONED, 로컬 커스텀 패턴 지원
     */
    private static LocalDateTime parseFlexibleDateTime(String s) {
        if (s == null) throw new DateTimeParseException("null", "", 0);
        String str = s.trim();
        if (str.isEmpty()) throw new DateTimeParseException("blank", s, 0);

        // 숫자만 → epoch (sec/ms)
        if (str.matches("^-?\\d+$")) {
            long epoch = Long.parseLong(str);
            Instant inst = (epoch >= 1_000_000_000_000L) ? Instant.ofEpochMilli(epoch) : Instant.ofEpochSecond(epoch);
            return LocalDateTime.ofInstant(inst, ZoneOffset.UTC);
        }

        // ISO_LOCAL (초 포함)
        try { return LocalDateTime.parse(str, DateTimeFormatter.ISO_LOCAL_DATE_TIME); }
        catch (DateTimeParseException ignore) {}

        // ISO_OFFSET → UTC
        try {
            OffsetDateTime odt = OffsetDateTime.parse(str, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            return odt.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        } catch (DateTimeParseException ignore) {}

        // ISO_ZONED → UTC
        try {
            ZonedDateTime zdt = ZonedDateTime.parse(str, DateTimeFormatter.ISO_ZONED_DATE_TIME);
            return zdt.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        } catch (DateTimeParseException ignore) {}

        // 로컬 패턴 (초 생략 허용)
        for (DateTimeFormatter f : FLEX_LOCAL_PATTERNS) {
            try { return LocalDateTime.parse(str, f); }
            catch (DateTimeParseException ignore) {}
        }

        throw new DateTimeParseException("unsupported datetime", s, 0);
    }

    /* ---------------- misc ---------------- */

    private void sendWsError(WebSocketSession session, ErrorCode ec) {
        try {
            String json = objectMapper.writeValueAsString(WsErrorMessage.of(ec));
            sessions.sendText(session, json);
        } catch (Exception ignore) {
            sessions.unregisterSession(session);
        }
    }

    private boolean isBlank(String s) { return s == null || s.isBlank(); }
    private String strOrNull(Object v) { return v == null ? null : String.valueOf(v); }
    private int parseIntSafe(Object v, int def) {
        try { return (v == null) ? def : Integer.parseInt(String.valueOf(v)); } catch (Exception ignore) { return def; }
    }
}

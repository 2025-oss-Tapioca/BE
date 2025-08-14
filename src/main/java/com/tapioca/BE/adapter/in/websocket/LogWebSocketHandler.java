package com.tapioca.BE.adapter.in.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tapioca.BE.adapter.out.mapper.LogMapper;
import com.tapioca.BE.adapter.out.websocketImpl.LogBroadCasterImpl;
import com.tapioca.BE.application.dto.response.log.LogResponseDto;
import com.tapioca.BE.application.service.log.LogService;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.config.exception.WsErrorMessage;
import com.tapioca.BE.domain.model.log.LogEntry;
import com.tapioca.BE.domain.model.log.SourceKey;
import com.tapioca.BE.domain.model.log.SourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MCP-BE ↔ BE 간 WebSocket 핸들러 (예외체계 407xx 적용판)
 *
 * 메시지 타입
 *  - "register"    : { sourceType: "BACKEND|FRONTEND|RDS", id: "<자연키>" }
 *  - "log"         : { data: { timestamp, level, service, message, ... } }
 *  - "filter"      : { from: "ISO-8601", to: "ISO-8601" }
 *  - "levelFilter" : { level: "ERROR", context: 10 }
 *
 * 예외 처리 정책
 *  - 입력 검증 실패/순서 오류 등 클라이언트 과실 → 407xx (Bad Request)
 *  - 핸들러 내부 예외(예상 밖) → 40750 (WS_INTERNAL_ERROR)
 *  - 응답 포맷은 WsErrorMessage(record)로 통일하여 클라이언트 일관성 확보
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LogWebSocketHandler extends TextWebSocketHandler {

    private final LogService logService;          // 원형 버퍼 관리/조회
    private final ObjectMapper objectMapper;      // 전역 Bean(자바타임 모듈 설정됨)
    private final LogBroadCasterImpl broadcaster; // 연결된 모든 세션으로 브로드캐스트

    /** sessionId → SourceKey 매핑 (register 이후에만 set) */
    private final Map<String, SourceKey> sessionKeyMap = new ConcurrentHashMap<>();

    /** context 상한: 과도한 요청 방지 */
    private static final int MAX_CONTEXT_LINES = 500;

    // ============================== 수명주기 ==============================

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("WS connected: {}", session.getId());
        broadcaster.addSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionKeyMap.remove(session.getId());
        broadcaster.removeSession(session);
        log.info("WS closed: {} / {}", session.getId(), status);
    }

    /**
     * 전송 오류/네트워크 오류 시 세션 정리 (누수 방지)
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.warn("WS transport error: session={} msg={}", safeId(session), exception.getMessage(), exception);
        sessionKeyMap.remove(session.getId());
        broadcaster.removeSession(session);
        safeClose(session);
    }

    // ============================== 메시지 진입점 ==============================

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            // 수신 JSON → Map (알 수 없는 필드는 전역 ObjectMapper에서 무시 설정)
            Map<String, Object> msg = objectMapper.readValue(message.getPayload(), Map.class);
            String type = (String) msg.get("type");

            if ("register".equals(type)) {
                handleRegister(session, msg);
            } else if ("log".equals(type)) {
                handleLog(session, msg);
            } else if ("filter".equals(type)) {
                handleFilter(session, msg);
            } else if ("levelFilter".equals(type)) {
                handleLevelFilter(session, msg);
            } else {
                throw new CustomException(ErrorCode.WS_UNKNOWN_TYPE); // 40700
            }

        } catch (CustomException ce) {
            // 도메인/입력 오류 등 → 클라이언트에 표준 포맷으로 회신
            wsErrorReply(session, ce.getErrorCode());
        } catch (Exception e) {
            // 예상 밖 예외 → 40750으로 통일
            log.error("WS error (session={}): {}", safeId(session), e.getMessage(), e);
            wsErrorReply(session, ErrorCode.WS_INTERNAL_ERROR); // 40750
        }
    }

    // ============================== 각 타입 처리 ==============================

    /** register 처리 */
    private void handleRegister(WebSocketSession session, Map<String, Object> msg) {
        String typeStr = strOrNull(msg.get("sourceType"));
        String id      = strOrNull(msg.get("id"));
        if (typeStr == null || id == null) {
            throw new CustomException(ErrorCode.WS_BAD_REGISTER); // 40701
        }

        try {
            SourceType type = SourceType.valueOf(typeStr.trim().toUpperCase());
            SourceKey key = new SourceKey(type, id.trim());

            SourceKey prev = sessionKeyMap.put(session.getId(), key);
            if (prev != null && !prev.equals(key)) {
                log.warn("WS register overrides previous source. session={}, prev={}, new={}", session.getId(), prev, key);
            }
            logService.registerServerBuffer(key); // 존재 시 유지, 없으면 생성
            log.info("Registered log source: {} (session={})", key, session.getId());

        } catch (IllegalArgumentException e) {
            // enum 매칭 실패(잘못된 sourceType)
            throw new CustomException(ErrorCode.WS_BAD_REGISTER); // 40701
        }
    }

    /** log 처리 */
    private void handleLog(WebSocketSession session, Map<String, Object> msg) throws Exception {
        SourceKey key = sessionKeyMap.get(session.getId());
        if (key == null) throw new CustomException(ErrorCode.WS_NOT_REGISTERED); // 40702

        Object dataObj = msg.get("data");
        if (!(dataObj instanceof Map<?, ?>)) {
            throw new CustomException(ErrorCode.WS_BAD_LOG_PAYLOAD); // 40703
        }
        Map<String, Object> raw = (Map<String, Object>) dataObj;

        LogEntry entry;
        try {
            entry = LogMapper.toDomainFromRawData(raw, objectMapper);
        } catch (IllegalArgumentException iae) {
            // timestamp 등 필수 필드 오류 상세는 서버 로그에만 남기고, 클라에는 표준 코드로 응답
            throw new CustomException(ErrorCode.WS_BAD_LOG_PAYLOAD); // 40703
        }

        logService.receiveLog(key, entry); // 버퍼에 적재

        // 표준 DTO로 브로드캐스트 (핸들러는 직렬화만 담당)
        LogResponseDto dto = LogMapper.toResponseDto(entry);
        broadcaster.broadcast(objectMapper.writeValueAsString(dto));

        log.debug("Log received from {}: {}", key, entry);
    }

    /** filter 처리 */
    private void handleFilter(WebSocketSession session, Map<String, Object> msg) throws Exception {
        SourceKey key = sessionKeyMap.get(session.getId());
        if (key == null) throw new CustomException(ErrorCode.WS_NOT_REGISTERED); // 40702

        String fromStr = strOrNull(msg.get("from"));
        String toStr   = strOrNull(msg.get("to"));
        if (fromStr == null || toStr == null) {
            throw new CustomException(ErrorCode.WS_INVALID_FILTER_DATE); // 40704
        }

        try {
            LocalDateTime from = LocalDateTime.parse(fromStr); // 입력은 ISO_LOCAL_DATE_TIME 기대
            LocalDateTime to   = LocalDateTime.parse(toStr);
            if (from.isAfter(to)) {
                throw new CustomException(ErrorCode.WS_BAD_FILTER_RANGE); // 40705
            }

            List<LogEntry> list = logService.getLogsBetween(key, from, to);
            for (LogEntry e : list) {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(
                        LogMapper.toResponseDto(e)
                )));
            }
            log.info("Filter sent. key={}, from={}, to={}, size={}", key, from, to, list.size());

        } catch (DateTimeParseException ex) {
            throw new CustomException(ErrorCode.WS_INVALID_FILTER_DATE); // 40704
        }
    }

    /** levelFilter 처리 */
    private void handleLevelFilter(WebSocketSession session, Map<String, Object> msg) throws Exception {
        SourceKey key = sessionKeyMap.get(session.getId());
        if (key == null) throw new CustomException(ErrorCode.WS_NOT_REGISTERED); // 40702

        String level = strOrNull(msg.get("level"));
        if (level == null || level.isBlank()) {
            throw new CustomException(ErrorCode.WS_INVALID_LEVEL_FILTER); // 40706
        }

        int requestedCtx = parseIntSafe(msg.get("context"), 0);
        int ctx = Math.max(0, Math.min(requestedCtx, MAX_CONTEXT_LINES));

        List<LogEntry> list = logService.getContextLogsByLevel(key, level, ctx);
        for (LogEntry e : list) {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(
                    LogMapper.toResponseDto(e)
            )));
        }
        log.info("LevelFilter sent. key={}, level={}, context(req={},use={}), size={}",
                key, level, requestedCtx, ctx, list.size());
    }

    // ============================== 유틸 ==============================

    /** 다양한 유형(Object)을 안전하게 int로 변환. 실패/Null이면 def 반환. */
    private int parseIntSafe(Object v, int def) {
        try {
            if (v instanceof Integer i) return i;
            if (v instanceof Double d)  return d.intValue();
            if (v instanceof String s)  return Integer.parseInt(s.trim());
        } catch (NumberFormatException ignored) {}
        return def;
    }

    /** Object → String(null-safe, trim 포함) */
    private String strOrNull(Object v) {
        if (v == null) return null;
        String s = (v instanceof String ss) ? ss : String.valueOf(v);
        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    /** WS 에러를 표준 포맷으로 회신 (네트워크 오류는 로그만) */
    private void wsErrorReply(WebSocketSession s, ErrorCode code) {
        try {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(objectMapper.writeValueAsString(
                        WsErrorMessage.of(code)
                )));
            }
        } catch (Exception sendEx) {
            log.warn("Failed to send WS error (session={}): {}", safeId(s), sendEx.getMessage());
        }
    }

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
}
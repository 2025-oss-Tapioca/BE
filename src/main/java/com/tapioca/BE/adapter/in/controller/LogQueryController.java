package com.tapioca.BE.adapter.in.controller;

import com.tapioca.BE.application.dto.response.log.LogResponseDto;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.model.log.*;
import com.tapioca.BE.domain.port.in.usecase.log.LogCollectionStatusUseCase;
import com.tapioca.BE.domain.port.in.usecase.log.QueryLogUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.Locale;

// 저장된 로그를 가져감 ( MCP Tool이 호출 할 때)
@RestController
@RequestMapping("/api/log/query")
@RequiredArgsConstructor
public class LogQueryController {

    private final QueryLogUseCase queryLogUseCase;
    private final LogCollectionStatusUseCase statusUseCase;

    private static final DateTimeFormatter ISO_LOCAL = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // ===== 기간 조회 =====
    @GetMapping("/range")
    public ResponseEntity<List<LogResponseDto>> getRange(
            @RequestParam String sourceType,
            @RequestParam String teamCode,
            @RequestParam String from,
            @RequestParam String to
    ) {
        SourceKey key = new SourceKey(SourceType.valueOf(sourceType.toUpperCase()), teamCode);

        // 버퍼 미생성 -> 에러
        if (!statusUseCase.isActive(key)) {
            throw new CustomException(ErrorCode.LOG_COLLECTION_REQUIRED);
        }

        LocalDateTime f = FlexibleDateTime.parse(from);
        LocalDateTime t = FlexibleDateTime.parse(to);
        if (f.isAfter(t)) throw new IllegalArgumentException("from must be <= to");

        List<LogEntry> list = queryLogUseCase.getLogsBetween(key, f, t);
        return ResponseEntity.ok(list.stream().map(LogQueryController::toDto).toList());
    }

    // ===== 레벨 컨텍스트 조회 =====
    @GetMapping("/context")
    public ResponseEntity<List<LogResponseDto>> getContext(
            @RequestParam String sourceType,
            @RequestParam String teamCode,
            @RequestParam String level,
            @RequestParam(defaultValue = "50") int context
    ) {
        SourceKey key = new SourceKey(SourceType.valueOf(sourceType.toUpperCase()), teamCode);

        // 버퍼 미생성 -> 에러
        if (!statusUseCase.isActive(key)) {
            throw new CustomException(ErrorCode.LOG_COLLECTION_REQUIRED);
        }

        int ctx = Math.max(0, Math.min(context, 500)); // 0~500 클램프
        List<LogEntry> list = queryLogUseCase.getContextLogsByLevel(key, level, ctx);
        return ResponseEntity.ok(list.stream().map(LogQueryController::toDto).toList());
    }

    private static LogResponseDto toDto(LogEntry e) {
        String ts = (e.getTimestamp() != null) ? e.getTimestamp().format(ISO_LOCAL) : null;
        String level = (e.getLevel() == null) ? null : e.getLevel().toUpperCase();
        String service = (e.getService() == null || e.getService().isBlank()) ? "-" : e.getService().trim();
        String message = (e.getMessage() == null) ? null : e.getMessage().trim();
        return new LogResponseDto(ts, level, service, message);
    }

    /** 동일 정책 유연 파서(WS/수집과 일치): ISO/OFFSET/ZONED, epoch(sec/ms), 공백/슬래시/점, 초 생략 허용 */
    static final class FlexibleDateTime {
        private static final DateTimeFormatter[] FLEX = new DateTimeFormatter[] {
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS", Locale.US),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS", Locale.US),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US),
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss", Locale.US),
                DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss", Locale.US),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.US),
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm", Locale.US),
                DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm", Locale.US),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", Locale.US)
        };

        static LocalDateTime parse(String s) {
            if (s == null || s.isBlank()) throw new DateTimeParseException("blank", s == null ? "" : s, 0);
            String str = s.trim();

            if (str.matches("^-?\\d+$")) { // epoch sec/ms
                long epoch = Long.parseLong(str);
                Instant inst = (epoch >= 1_000_000_000_000L) ? Instant.ofEpochMilli(epoch) : Instant.ofEpochSecond(epoch);
                return LocalDateTime.ofInstant(inst, ZoneOffset.UTC);
            }
            try { return LocalDateTime.parse(str, DateTimeFormatter.ISO_LOCAL_DATE_TIME); } catch (Exception ignore) {}
            try { return OffsetDateTime.parse(str, DateTimeFormatter.ISO_OFFSET_DATE_TIME).atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime(); } catch (Exception ignore) {}
            try { return ZonedDateTime.parse(str, DateTimeFormatter.ISO_ZONED_DATE_TIME).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime(); } catch (Exception ignore) {}
            for (DateTimeFormatter f : FLEX) { try { return LocalDateTime.parse(str, f); } catch (Exception ignore) {} }
            throw new DateTimeParseException("unsupported datetime", s, 0);
        }
    }
}

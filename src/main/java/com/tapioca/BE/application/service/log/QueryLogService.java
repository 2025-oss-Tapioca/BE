package com.tapioca.BE.application.service.log;

import com.tapioca.BE.domain.model.log.CircularLogBuffer;
import com.tapioca.BE.domain.model.log.LogEntry;
import com.tapioca.BE.domain.model.log.SourceKey;
import com.tapioca.BE.domain.port.in.usecase.log.QueryLogUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * QueryLogUseCase 구현체
 * - PushLogService가 관리하는 동일 버퍼를 조회
 */
@Service
@RequiredArgsConstructor
public class QueryLogService implements QueryLogUseCase {

    private final PushLogService pushLogService; // 동일 버퍼 공유

    @Override
    public List<LogEntry> getLogsBetween(SourceKey key, LocalDateTime from, LocalDateTime to) {
        CircularLogBuffer buf = pushLogService.getBuffer(key);
        if (buf == null || from == null || to == null) return List.of();


        return buf.getLogsBetween(from, to);
    }

    @Override
    public List<LogEntry> getContextLogsByLevel(SourceKey key, String level, int contextLines) {
        CircularLogBuffer buf = pushLogService.getBuffer(key);
        if (buf == null || level == null || level.isBlank()) return List.of();


        return buf.getContextLogsByLevel(level, contextLines);
    }
}
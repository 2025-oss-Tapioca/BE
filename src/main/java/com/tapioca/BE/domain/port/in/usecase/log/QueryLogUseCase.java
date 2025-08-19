package com.tapioca.BE.domain.port.in.usecase.log;

import com.tapioca.BE.domain.model.log.LogEntry;
import com.tapioca.BE.domain.model.log.SourceKey;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 로그 조회 유즈케이스
 * - 기간/레벨/컨텍스트 등 조회 기능 제공
 */
public interface QueryLogUseCase {
    List<LogEntry> getLogsBetween(SourceKey key, LocalDateTime from, LocalDateTime to);      // 기간 조회
    List<LogEntry> getContextLogsByLevel(SourceKey key, String level, int contextLines);     // 레벨 컨텍스트
}
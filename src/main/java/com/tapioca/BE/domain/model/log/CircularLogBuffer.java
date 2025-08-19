package com.tapioca.BE.domain.model.log;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * [원형 로그 버퍼 - 동기화 버전]
 * - 최근 N개의 로그를 고정 크기로 보관 (오래된 항목 overwrite)
 * - 멀티스레드 환경에서 안전하도록 메서드 단위 동기화(synchronized) 적용
 */
public class CircularLogBuffer {

    /** 로그 저장 배열 (capacity 고정) */
    private final LogEntry[] buffer;

    /** 버퍼 최대 크기 */
    private final int capacity;

    /** 다음 로그가 저장될 인덱스 (0 ~ capacity-1, 순환) */
    private int pointer = 0;

    /** 버퍼가 한 바퀴 돌아서 꽉 찬 상태인지 여부 */
    private boolean isBufferFull = false;

    /** 생성자 */
    public CircularLogBuffer(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("capacity must be positive");
        this.capacity = capacity;
        this.buffer = new LogEntry[capacity];
    }

    /** 로그 추가 (쓰기) */
    public synchronized void addLog(LogEntry logEntry) {
        if (logEntry == null) return;
        buffer[pointer] = logEntry;                  // 새 로그 기록
        pointer = (pointer + 1) % capacity;         // 다음 칸
        if (pointer == 0) {
            isBufferFull = true;                     // 한 바퀴 돌았음
        }
    }

    /** 시간 구간 조회 (읽기) */
    public synchronized List<LogEntry> getLogsBetween(LocalDateTime from, LocalDateTime to) {
        List<LogEntry> result = new ArrayList<>();
        if (from == null || to == null) return result;

        int startIdx = isBufferFull ? pointer : 0;
        int logsToCheck = isBufferFull ? capacity : pointer;

        for (int i = 0; i < logsToCheck; i++) {
            int index = (startIdx + i) % capacity;
            LogEntry entry = buffer[index];
            if (entry != null &&
                    !entry.getTimestamp().isBefore(from) &&   // from 이상
                    !entry.getTimestamp().isAfter(to)) {      // to 이하
                result.add(entry);
            }
        }
        return result;
    }

    /**
     * 레벨 기반 컨텍스트 로그 조회 (읽기)
     * - 첫 매칭 로그의 "이전 contextLines개 + 해당 로그"를 반환
     */
    public synchronized List<LogEntry> getContextLogsByLevel(String level, int contextLines) {
        List<LogEntry> result = new ArrayList<>();
        if (level == null || level.isBlank()) return result;

        int startIdx = isBufferFull ? pointer : 0;
        int logsToCheck = isBufferFull ? capacity : pointer;

        for (int i = 0; i < logsToCheck; i++) {
            int index = (startIdx + i) % capacity;
            LogEntry entry = buffer[index];

            if (entry != null && level.equalsIgnoreCase(entry.getLevel())) {
                int ctx = Math.max(0, contextLines);
                int contextStart = (index - ctx + capacity) % capacity;

                for (int j = 0; j <= ctx; j++) {
                    int ctxIdx = (contextStart + j) % capacity;
                    LogEntry ctxEntry = buffer[ctxIdx];
                    if (ctxEntry != null) {
                        result.add(ctxEntry);
                    }
                }
                break; // 첫 매칭만 반환
            }
        }
        return result;
    }
}
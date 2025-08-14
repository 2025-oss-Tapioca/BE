package com.tapioca.BE.domain.model.log;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * [원형 로그 버퍼]
 * - 메모리 내에서 최근 N개의 로그를 고정 크기로 보관
 * - 오래된 로그는 새로운 로그가 들어오면 덮어씀(Overwrite)
 * - 포인터 기반 원형 순환 구조
 *
 * 주요 특징
 * ─────────────────────────────────────────────
 * 1) capacity를 초과하면 가장 오래된 로그부터 덮어쓰기
 * 2) getLogsBetween(): 시간 구간별 로그 조회
 * 3) getContextLogsByLevel(): 특정 레벨 로그 발견 시 앞쪽 contextLines 라인 포함 조회
 *
 * 설계 이유
 * ─────────────────────────────────────────────
 * - 로그는 무한히 쌓일 수 있으므로 메모리에 전부 보관 불가
 * - 최근 로그 위주로 실시간 분석/조회 용도로 사용
 * - 동기화는 상위 서비스에서 책임짐 (이 클래스 자체는 thread-safe 아님)
 */
public class CircularLogBuffer {

    /** 로그 저장 배열 (capacity 크기 고정) */
    private final LogEntry[] buffer;

    /** 버퍼 최대 크기 */
    private final int capacity;

    /** 다음 로그가 저장될 인덱스 (0 ~ capacity-1, 순환) */
    private int pointer = 0;

    /** 버퍼가 한 바퀴 돌아서 꽉 찬 상태인지 여부 */
    private boolean isBufferFull = false;

    /**
     * 생성자
     *
     * @param capacity 저장 가능한 최대 로그 수
     */
    public CircularLogBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new LogEntry[capacity];
    }

    /**
     * [로그 추가]
     * - pointer 위치에 새 로그를 저장
     * - capacity를 초과하면 가장 오래된 로그부터 덮어씀
     *
     * @param logEntry 새로 추가할 로그
     */
    public void addLog(LogEntry logEntry) {
        buffer[pointer] = logEntry;
        pointer = (pointer + 1) % capacity; // 다음 인덱스로 이동
        if (pointer == 0) {
            isBufferFull = true; // 한 바퀴 돌면 이후 계속 true
        }
    }

    /**
     * [시간 구간 조회]
     * - from ~ to 범위에 포함되는 로그를 시간 순서대로 반환
     * - 원형 순환 상태를 고려하여 올바른 시간 순서로 순회
     *
     * @param from 시작 시간(포함)
     * @param to   종료 시간(포함)
     * @return 시간 범위 내 로그 목록
     */
    public List<LogEntry> getLogsBetween(LocalDateTime from, LocalDateTime to) {
        List<LogEntry> result = new ArrayList<>();

        // 버퍼가 가득 찼으면 pointer부터 순회, 아니면 0부터
        int startIdx = isBufferFull ? pointer : 0;
        // 순회해야 할 로그 개수 (버퍼가 꽉 찼으면 capacity, 아니면 현재 pointer)
        int logsToCheck = isBufferFull ? capacity : pointer;

        for (int i = 0; i < logsToCheck; i++) {
            int index = (startIdx + i) % capacity; // 원형 인덱스 계산
            LogEntry entry = buffer[index];
            if (entry != null &&
                    !entry.getTimestamp().isBefore(from) && // from 이상
                    !entry.getTimestamp().isAfter(to)) {    // to 이하
                result.add(entry);
            }
        }
        return result;
    }

    /**
     * [레벨 기반 컨텍스트 로그 조회]
     * - 특정 로그 레벨이 처음 발생한 지점을 기준으로
     *   앞쪽 contextLines 만큼 + 해당 로그까지 반환
     * - 첫 매칭 로그까지만 검색
     *
     * @param level        찾고자 하는 로그 레벨 (예: "ERROR", "WARN")
     * @param contextLines 매칭 로그 이전에 포함할 로그 수
     * @return 컨텍스트 포함 로그 목록
     */
    public List<LogEntry> getContextLogsByLevel(String level, int contextLines) {
        List<LogEntry> result = new ArrayList<>();

        int startIdx = isBufferFull ? pointer : 0;
        int logsToCheck = isBufferFull ? capacity : pointer;

        for (int i = 0; i < logsToCheck; i++) {
            int index = (startIdx + i) % capacity;
            LogEntry entry = buffer[index];

            if (entry != null && level.equalsIgnoreCase(entry.getLevel())) {
                // 매칭된 로그 발견 시 → 앞 contextLines 개수 계산
                int contextStart = (index - contextLines + capacity) % capacity;

                for (int j = 0; j <= contextLines; j++) {
                    int ctxIdx = (contextStart + j) % capacity;
                    LogEntry ctxEntry = buffer[ctxIdx];
                    if (ctxEntry != null) {
                        result.add(ctxEntry);
                    }
                }
                break; // 첫 매칭 후 종료
            }
        }
        return result;
    }
}

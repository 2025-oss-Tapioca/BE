package com.tapioca.BE.application.service.log;

import com.tapioca.BE.domain.model.log.CircularLogBuffer;
import com.tapioca.BE.domain.model.log.LogEntry;
import com.tapioca.BE.domain.model.log.SourceKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 소스별(백/프론트/RDS) 로그 버퍼 관리 서비스.
 *
 * 핵심 개념
 * ─────────────────────────────────────────────────────────────────────────────
 * - Key   : SourceKey(type, id)  ← "소스유형+자연키" 조합으로 유일 식별
 * - Value : CircularLogBuffer     ← 고정 크기 원형 버퍼(오래된 로그부터 덮어씀)
 *
 * 설계 포인트
 * ─────────────────────────────────────────────────────────────────────────────
 * 1) 동시성: ConcurrentHashMap + 내부 CircularBuffer가 단일 스레드 쓰기 가정 없이도 안전하도록 사용
 * 2) 용량: 기본 50,000건.
 * 3) 방어: null 파라미터는 즉시 무시/빈 리스트 반환(상위 계층에서 에러를 이미 관리)
 * 4) 조회:
 *    - getLogsBetween: 시간대 필터
 *    - getContextLogsByLevel: 특정 레벨 최초 매칭 지점 기준 컨텍스트
 *
 */
@Service
public class LogService {

    /** 기본 버퍼 용량(건수). yml에서 logging.buffer-capacity로 재정의 가능 */
    @Value("${logging.buffer-capacity:50000}")
    private int configuredCapacity;

    /**
     * 소스별 원형 버퍼 저장소.
     * - computeIfAbsent 로 lazy 생성
     * - 세션이 등록(register)될 때 미리 생성됨
     */
    private final Map<SourceKey, CircularLogBuffer> buffers = new ConcurrentHashMap<>();

    // ─────────────────────────────────────────────────────────────────────
    // 등록/수신
    // ─────────────────────────────────────────────────────────────────────

    /**
     * 소스 등록: 해당 소스의 원형 버퍼를 보장 생성.
     * 이미 존재하면 재사용.
     *
     * @param key SourceKey(type, id)
     */
    public void registerServerBuffer(SourceKey key) {
        if (key == null) return;
        buffers.computeIfAbsent(key, k -> new CircularLogBuffer(normalizedCapacity()));
    }

    /**
     * 로그 수신: 해당 소스의 버퍼에 로그를 적재.
     * 버퍼가 없으면(drop) → 상위(WS 핸들러)가 register 선행을 강제하므로 정상 흐름에선 거의 없음.
     *
     * @param key      SourceKey(type, id)
     * @param logEntry 수신 로그
     */
    public void receiveLog(SourceKey key, LogEntry logEntry) {
        if (key == null || logEntry == null) return;
        CircularLogBuffer buffer = buffers.get(key);
        if (buffer != null) {
            buffer.addLog(logEntry);
        }
        // buffer가 null이면 조용히 drop. (원하면 warn 로그로 바꿀 수 있음)
    }

    // ─────────────────────────────────────────────────────────────────────
    // 조회
    // ─────────────────────────────────────────────────────────────────────

    /**
     * 지정된 시간 구간의 로그 조회(시간 오름차순).
     *
     * @param key  SourceKey(type, id)
     * @param from 시작 시각(포함)
     * @param to   종료 시각(포함)
     * @return 구간 내 로그 리스트(버퍼 없으면 빈 리스트)
     */
    public List<LogEntry> getLogsBetween(SourceKey key, LocalDateTime from, LocalDateTime to) {
        if (key == null || from == null || to == null) return List.of();
        CircularLogBuffer buffer = buffers.get(key);
        return (buffer != null) ? buffer.getLogsBetween(from, to) : List.of();
    }

    /**
     * 특정 레벨 최초 매칭 지점 기준 컨텍스트 로그 조회.
     * (컨텍스트는 CircularLogBuffer 구현에 따름. 최초 매칭까지만 반환)
     *
     * @param key          SourceKey(type, id)
     * @param level        로그 레벨(대소문자 무시 처리: 내부 equalsIgnoreCase 사용 전제)
     * @param contextLines 기준 로그 "앞"으로 몇 줄까지 포함할지(음수는 0으로 치환)
     * @return 컨텍스트 포함 로그 리스트(버퍼 없으면 빈 리스트)
     */
    public List<LogEntry> getContextLogsByLevel(SourceKey key, String level, int contextLines) {
        if (key == null || level == null) return List.of();
        CircularLogBuffer buffer = buffers.get(key);
        int safeCtx = Math.max(0, contextLines);
        return (buffer != null) ? buffer.getContextLogsByLevel(level, safeCtx) : List.of();
    }

    // ─────────────────────────────────────────────────────────────────────
    // 선택 유틸(모니터링/정리) — 필요 시 사용
    // ─────────────────────────────────────────────────────────────────────

    /**
     * (선택) 소스 버퍼 제거.
     * - 세션이 완전히 해제되고, 해당 소스가 더 이상 들어오지 않는 것이 보장될 때 호출
     */
    public void unregisterSource(SourceKey key) {
        if (key == null) return;
        buffers.remove(key);
    }

    /**
     * 현재 관리 중인 소스 버퍼 개수.
     */
    public int getManagedBufferCount() {
        return buffers.size();
    }



    /**
     * 외부 설정값을 안전하게 정규화.
     * - 1 미만이 설정되면 기본값(50,000)로 되돌림
     */
    private int normalizedCapacity() {
        return (configuredCapacity >= 1) ? configuredCapacity : 50_000;
    }
}

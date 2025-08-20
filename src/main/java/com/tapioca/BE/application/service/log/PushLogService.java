package com.tapioca.BE.application.service.log;

import com.tapioca.BE.domain.model.log.CircularLogBuffer;
import com.tapioca.BE.domain.model.log.LogEntry;
import com.tapioca.BE.domain.model.log.SourceKey;
import com.tapioca.BE.domain.port.in.usecase.log.PushLogUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * PushLogUseCase 구현체
 * - 실시간 로그를 소스별 원형버퍼에 적재
 * - 내부적으로 가벼운 idle-eviction(유휴 버퍼 청소)을 자동 수행
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PushLogService implements PushLogUseCase {

    @Value("${logging.buffer-capacity:50000}")
    private int bufferCapacity; // 버퍼 용량(기본 50,000)

    @Value("${logging.buffer-idle-ttl-ms:1800000}")
    private long idleTtlMs;     // 마지막 접근 후 TTL(ms) 초과 시 버퍼 제거 (기본 30분)

    @Value("${logging.buffer-evict-every-n-pushes:100}")
    private long evictEveryNPushed; // push N회마다 한 번 청소 (기본 100)

    /** SourceKey → BufferHolder(버퍼 + 최근 접근 시각) */
    private final Map<SourceKey, BufferHolder> buffers = new ConcurrentHashMap<>();

    /** push 누적 카운터 (N회마다 evict 트리거) */
    private final AtomicLong pushCount = new AtomicLong(0);

    @Override
    public void push(SourceKey key, LogEntry entry) {
        if (key == null || entry == null) return;

        // 없으면 생성, 있으면 재사용 (경합에도 안전)
        BufferHolder holder = buffers.computeIfAbsent(
                key,
                k -> new BufferHolder(new CircularLogBuffer(bufferCapacity))
        );

        holder.buffer.addLog(entry);                  // 원형 버퍼에 추가
        holder.lastTouched = System.currentTimeMillis(); // 최근 접근 갱신

        // N회마다 가벼운 idle 청소 자동 수행
        long n = pushCount.incrementAndGet();
        if (n % Math.max(1, evictEveryNPushed) == 0) {
            evictIdleBuffers(); // 반환값은 로깅 용으로만 사용
        }
    }

    /** 같은 버퍼를 QueryLogService가 조회할 수 있게 공개(패키지 내부) */
    CircularLogBuffer getBuffer(SourceKey key) {
        BufferHolder holder = buffers.get(key);
        return (holder == null) ? null : holder.buffer;
    }

    // ===================== 운영/관리용 보조 메서드 (패키지 내부 공개) =====================

    /** 즉시 idle eviction 수행(운영 서비스가 수동 호출) → 제거된 버퍼 수 반환 */
    int evictIdleBuffersNow() {
        return evictIdleBuffers();
    }

    /** 현재 유지 중인 버퍼 개수 */
    int buffersSize() {
        return buffers.size();
    }

    /** 특정 소스의 버퍼 제거 (제거되면 true) */
    boolean removeBuffer(SourceKey key) {
        return buffers.remove(key) != null;
    }

    /** 모든 버퍼 제거 후 제거 개수 반환 */
    int clearAllBuffers() {
        int sz = buffers.size();
        buffers.clear();
        return sz;
    }

    // ===================== 내부 구현 =====================

    /** idle TTL을 초과한 버퍼를 제거하고, 제거된 개수를 반환 */
    private int evictIdleBuffers() {
        final long now = System.currentTimeMillis();
        int removed = 0;

        for (Map.Entry<SourceKey, BufferHolder> e : buffers.entrySet()) {
            BufferHolder h = e.getValue();
            if (h == null) continue;

            if (now - h.lastTouched > idleTtlMs) {
                if (buffers.remove(e.getKey(), h)) {
                    removed++;
                }
            }
        }

        if (removed > 0 && log.isDebugEnabled()) {
            log.debug("[PushLogService] evicted {} idle buffers (ttl={}ms, remaining={})",
                    removed, idleTtlMs, buffers.size());
        }
        return removed;
    }

    /** 버퍼 + 최근 접근 시각 보관용 홀더 */
    private static final class BufferHolder {
        final CircularLogBuffer buffer;
        volatile long lastTouched;
        BufferHolder(CircularLogBuffer buffer) {
            this.buffer = buffer;
            this.lastTouched = System.currentTimeMillis();
        }
    }
}
package com.tapioca.BE.domain.port.in.usecase.log;

import com.tapioca.BE.domain.model.log.LogEntry;
import com.tapioca.BE.domain.model.log.SourceKey;

/**
 * 실시간 로그 수신 유즈케이스
 * - WS(MessageHandler)가 호출
 */
public interface PushLogUseCase {
    void push(SourceKey key, LogEntry entry);                   // 버퍼 적재(+ 브로드캐스트는 외부에서)
}
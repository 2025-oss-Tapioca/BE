package com.tapioca.BE.domain.port.in.usecase.log;

import com.tapioca.BE.domain.model.log.LogEntry;
import com.tapioca.BE.domain.model.log.SourceKey;

import java.util.Map;

/**
 * WS 등에서 들어온 원시 로그(raw JSON/Map)를 파싱하여 도메인(LogEntry)로 변환하고 버퍼에 적재하는 유즈케이스.
 */
public interface IngestRawLogUseCase {
    LogEntry ingest(SourceKey key, Map<String, Object> rawData);
}
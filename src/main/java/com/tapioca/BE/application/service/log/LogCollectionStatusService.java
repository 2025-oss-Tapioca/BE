package com.tapioca.BE.application.service.log;

import com.tapioca.BE.domain.model.log.SourceKey;
import com.tapioca.BE.domain.port.in.usecase.log.LogCollectionStatusUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


// 버퍼가 비었는지 확인하는 용도 (MCP가 호출할 때 로그 수집이 시작되지 않은 상태면 알림)
@Service
@RequiredArgsConstructor
public class LogCollectionStatusService implements LogCollectionStatusUseCase {
    private final PushLogService pushLogService; // 같은 패키지라 getBuffer 접근 가능(패키지 접근자)

    @Override
    public boolean isActive(SourceKey key) {
        return pushLogService.getBuffer(key) != null;
    }
}

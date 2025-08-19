package com.tapioca.BE.application.service.log;

import com.tapioca.BE.domain.model.log.SourceKey;
import com.tapioca.BE.domain.port.in.usecase.log.ManageLogBuffersUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**  로그 버퍼 운영/관리 서비스 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ManageLogBuffersService implements ManageLogBuffersUseCase {

    private final PushLogService pushLogService;

    @Override
    public int size() {
        // 현재 버퍼 개수 반환 (패키지 내부 보조 메서드 호출)
        return pushLogService.buffersSize();
    }

    @Override
    public boolean clear(SourceKey key) {
        // 특정 소스 버퍼 제거 (패키지 내부 보조 메서드 호출)
        return pushLogService.removeBuffer(key);
    }

    @Override
    public int clearAll() {
        // 전체 버퍼 제거 후 제거 개수 반환 (패키지 내부 보조 메서드 호출)
        return pushLogService.clearAllBuffers();
    }
}
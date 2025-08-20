package com.tapioca.BE.domain.port.in.usecase.log;

import com.tapioca.BE.domain.model.log.SourceKey;

public interface LogCollectionStatusUseCase {
    boolean isActive(SourceKey key); // 해당 SourceKey의 버퍼가 존재/활성?
}

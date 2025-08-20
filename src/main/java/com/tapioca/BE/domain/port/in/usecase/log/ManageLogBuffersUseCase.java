package com.tapioca.BE.domain.port.in.usecase.log;

import com.tapioca.BE.domain.model.log.SourceKey;

/**
 * 로그 버퍼 운영/관리 유즈케이스
 * - 여기서는 운영 편의 기능(현황/초기화)만 노출
 */
public interface ManageLogBuffersUseCase {

    /** 현재 유지 중인 버퍼(소스) 개수 반환 */
    int size();

    /** 특정 소스의 버퍼 제거 (true=제거됨) */
    boolean clear(SourceKey key);

    /** 모든 버퍼 제거, 제거된 개수 반환 */
    int clearAll();
}
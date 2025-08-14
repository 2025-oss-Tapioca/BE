package com.tapioca.BE.domain.model.log;

import java.util.Objects;

/**
 * 내부 식별 키. 버퍼/세션 맵에서 Key로 사용된다.
 * - type: 소스 유형(BACKEND/FRONTEND/RDS)
 * - id  : 자연키(BACKEND/FRONTEND=host, RDS=rdsInstanceId 또는 dbAddress)
 */
public final class SourceKey {
    private final SourceType type;
    private final String id;

    public SourceKey(SourceType type, String id) {
        this.type = type;
        this.id = id;
    }
    public SourceType type() { return type; }
    public String id()       { return id; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SourceKey that)) return false;
        return type == that.type && Objects.equals(id, that.id);
    }
    @Override public int hashCode() { return Objects.hash(type, id); }
    @Override public String toString() { return type + "|" + id; } // 예: "FRONTEND|3.38.xxx.xxx"
}

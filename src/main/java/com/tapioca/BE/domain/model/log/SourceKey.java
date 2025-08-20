package com.tapioca.BE.domain.model.log;

import java.util.Objects;

/**
 * [내부 식별 키]
 * - 버퍼/세션 맵에서 Key로 사용
 * - 구성: (type, code)
 *   · type : 소스 유형(BACKEND / FRONTEND / RDS)
 *   · code : 팀 코드(팀당 BACK/FRONT/RDS 1개라는 전제에서 자연키 역할)
 */
public final class SourceKey {

    private final SourceType type;   // 소스 유형
    private final String code;       // 팀 코드

    public SourceKey(SourceType type, String code) {
        this.type = type;
        this.code = code;
    }

    // ----- Accessors -----
    public SourceType type() {
        return type;
    }

    public String code() {
        return code;
    }

    // ----- Equality & Hash -----
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SourceKey that)) return false;
        return type == that.type && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, code);
    }

    // ----- Debug Friendly -----
    @Override
    public String toString() {
        return type + "|" + code; // 예: "FRONTEND|TEAM1234"
    }
}
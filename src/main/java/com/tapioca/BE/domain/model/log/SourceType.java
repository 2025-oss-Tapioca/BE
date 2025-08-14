package com.tapioca.BE.domain.model.log;

/**
 * 로그 소스 유형을 구분하기 위한 열거형.
 * TYPE + 자연키(id) 조합만으로 식별한다.
 */
public enum SourceType {
    BACKEND,   // 백엔드 애플리케이션 서버
    FRONTEND,  // 프론트엔드(웹 서버/FE 런타임)
    RDS        // 데이터베이스(RDS/CloudWatch 구독 등)
}

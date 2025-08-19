package com.tapioca.BE.config.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 로깅 과정 직렬화 등 전역 Jackson 설정
 * - JavaTimeModule 등록: LocalDateTime 등 JSR-310 타입 지원
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> builder
                .modules(new JavaTimeModule()) // JSR-310 날짜/시간 지원
                // ↓ 오버로드 분리 (타입별로 호출)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // 타임스탬프 대신 ISO 문자열
                .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // 모르는 필드 무시
    }
}
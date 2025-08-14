package com.tapioca.BE.config.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 로깅과정에서 직렬화를 위해 사용
 * Jackson(ObjectMapper) 전역 설정.
 * ✓ JavaTimeModule 등록: LocalDateTime 등 JSR-310 타입 지원
 * ✓ WRITE_DATES_AS_TIMESTAMPS 비활성화: 날짜를 숫자 타임스탬프가 아닌 ISO-8601 문자열로 직렬화
 * ✓ FAIL_ON_UNKNOWN_PROPERTIES 비활성화: 예상치 못한 필드가 와도 무시하고 역직렬화
 * - ObjectMapper 자체를 새로 빈 등록하지 않고, 커스터마이저로 부트의 기본 mapper에 옵션만 추가.
 *   (덮어쓰기/빈 충돌 방지)
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> builder
                .modules(new JavaTimeModule())
                .featuresToDisable(
                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,  // ISO 문자열 사용
                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES // 알 수 없는 필드 무시
                );
    }

}

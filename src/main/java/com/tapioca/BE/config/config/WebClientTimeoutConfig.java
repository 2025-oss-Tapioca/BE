package com.tapioca.BE.config.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * [WebClient 전역 설정 — 타임아웃 강화판]
 * 운영 환경에서 "응답 지연/무한대기"를 방지하기 위해 커넥션·응답·I/O 타임아웃을 명시한다.
 *
 * - connect timeout : TCP 핸드셰이크 지연 방지 (예: MCP가 다운/네트워크 단절 시 즉시 감지)
 * - response timeout: HTTP 응답 헤더 지연 방지 (서버가 응답을 너무 늦게 줄 때)
 * - read/write timeout: 소켓 레벨 데이터 송수신 지연 방지 (스트림이 멈춰 있을 때)
 *
 */
@Configuration
public class WebClientTimeoutConfig {

    private static final int CONNECT_TIMEOUT_MS = 3_000;  // TCP 연결 타임아웃
    private static final int READ_TIMEOUT_MS    = 5_000;  // 소켓 read 타임아웃
    private static final int WRITE_TIMEOUT_MS   = 5_000;  // 소켓 write 타임아웃
    private static final Duration RESPONSE_TIMEOUT = Duration.ofSeconds(5); // 응답 헤더 타임아웃

    @Bean
    public WebClient webClient() {
        // 1) Reactor Netty HttpClient에 타임아웃/핸들러 설정
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT_MS)
                .responseTimeout(RESPONSE_TIMEOUT)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(READ_TIMEOUT_MS, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(WRITE_TIMEOUT_MS, TimeUnit.MILLISECONDS))
                );

        // 2) 메시지 버퍼 크기 조정 (대용량 JSON 대비 — 필요 시)
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(cfg -> cfg.defaultCodecs().maxInMemorySize(2 * 1024 * 1024)) // 2MB
                .build();

        // 3) WebClient 빌더에 baseUrl과 커넥터 주입
        return WebClient.builder()
                .baseUrl("http://mcp-service:8080")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(strategies)
                .build();
    }
}
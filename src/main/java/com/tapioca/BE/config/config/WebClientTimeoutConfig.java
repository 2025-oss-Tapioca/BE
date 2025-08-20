package com.tapioca.BE.config.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * WebClient for MCP calls
 * - Connection Pool 설정
 * - 명시적 Bean 이름(mcpWebClient)
 * - HTTPS(또는 WSS) 사용 시: baseUrl을 https:// 로만 바꾸면 자동 TLS
 */
@Configuration
public class WebClientTimeoutConfig {

    private static final int CONNECT_TIMEOUT_MS = 3_000;
    private static final int READ_TIMEOUT_MS    = 5_000;
    private static final int WRITE_TIMEOUT_MS   = 5_000;
    private static final Duration RESPONSE_TIMEOUT = Duration.ofSeconds(5);

    @Value("${logging.mcp-base-url:http://mcp-service:8080}")
    private String mcpBaseUrl;

    @Bean(name = "mcpWebClient") // ← Bean 이름 명시
    public WebClient mcpWebClient() {
        // 커넥션 풀
        ConnectionProvider pool = ConnectionProvider.builder("mcp-pool")
                .maxConnections(100)
                .pendingAcquireMaxCount(1000)
                .pendingAcquireTimeout(Duration.ofSeconds(5))
                .maxIdleTime(Duration.ofSeconds(30))
                .maxLifeTime(Duration.ofMinutes(5))
                .build();

        // HTTPS 사용 시(baseUrl이 https://) 자동으로 TLS 사용됨
        // (셀프사인 인증서 신뢰가 필요하면 별도의 SslProvider 설정이 필요하지만 여기서는 다루지 않음)
        HttpClient httpClient = HttpClient.create(pool)
                .compress(true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT_MS)
                .responseTimeout(RESPONSE_TIMEOUT)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(READ_TIMEOUT_MS, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(WRITE_TIMEOUT_MS, TimeUnit.MILLISECONDS))
                );

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(cfg -> cfg.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                .build();

        return WebClient.builder()
                .baseUrl(mcpBaseUrl) // http:// or https:// 둘 다 OK
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(strategies)
                .build();
    }
}
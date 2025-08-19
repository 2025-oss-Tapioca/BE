package com.tapioca.BE.adapter.out.httpImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tapioca.BE.application.dto.request.mcp.McpLogRegisterRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class McpLogClientImpl implements McpLogClient {

    private final @Qualifier("mcpWebClient") WebClient webClient;
    private final ObjectMapper objectMapper;
    private final WebClientErrorTranslator translator;

    @Value("${logging.mcp-register-path:/mcp/logging/register}")
    private String registerPath;

    @Value("${logging.shared-secret}")
    private String sharedSecret;

    @Override
    public void registerLogSource(McpLogRegisterRequestDto dto) {
        final String ctx = "MCP register[" + dto.getType() + "|" + dto.getInternalKey() + "]";
        try {
            byte[] body = objectMapper.writeValueAsBytes(dto);
            String signature = hmacSha256Hex(sharedSecret, body);

            webClient.post()
                    .uri(registerPath)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-Signature", signature)
                    .bodyValue(body)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, resp -> resp.createException().flatMap(Mono::error))
                    .toBodilessEntity()
                    .block();

            log.info("[MCP] register OK: type={}, key={}", dto.getType(), dto.getInternalKey());
        } catch (Throwable ex) {
            throw translator.translate(ex, ctx);
        }
    }

    private static String hmacSha256Hex(String secret, byte[] body) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] out = mac.doFinal(body);

        StringBuilder sb = new StringBuilder(out.length * 2);
        for (byte b : out) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
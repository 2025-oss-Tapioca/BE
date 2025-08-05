package com.tapioca.BE.application.service.gptCommon;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SendToMcpService {

    @Value("${mcp.server.url}")
    private String mcpUrl;

    private final RestTemplate restTemplate;

    // mcp 서버로 json을 보내는 메서드
    public Object sendRequestToMcp(JsonNode gptJson){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<JsonNode> entity=new HttpEntity<>(gptJson,headers);
        return restTemplate.postForEntity(mcpUrl,entity,Object.class).getBody();
    }
}

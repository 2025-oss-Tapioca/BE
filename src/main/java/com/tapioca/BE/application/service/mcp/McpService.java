package com.tapioca.BE.application.service.mcp;

import com.tapioca.BE.adapter.out.mapper.McpMapper;
import com.tapioca.BE.application.dto.request.mcp.McpRequestDto;
import com.tapioca.BE.application.dto.response.mcp.McpResponseDto;
import com.tapioca.BE.domain.model.Mcp;
import com.tapioca.BE.domain.port.in.usecase.mcp.McpUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

@Service
@RequiredArgsConstructor
public class McpService implements McpUseCase {

    @Value("${mcp.server.url}")
    private String mcpUrl;

    private final RestTemplate restTemplate;
    private final McpMapper mcpMapper;

    @Override
    public ResponseEntity<McpResponseDto> sendRequestToMcp(McpRequestDto mcpRequestDto){
        Mcp mcp=mcpMapper.toDomain(mcpRequestDto);

        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = "{\"userRequest\" : \"" +mcp.getUserRequest()+"\"}";

        HttpEntity<String> entity=new HttpEntity<>(json,headers);
        return restTemplate.postForEntity(mcpUrl,entity,McpResponseDto.class);
    }
}

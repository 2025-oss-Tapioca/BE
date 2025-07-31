package com.tapioca.BE.application.service.gpt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tapioca.BE.adapter.out.mapper.McpMapper;
import com.tapioca.BE.application.dto.request.gpt.GptRequestDto;
import com.tapioca.BE.application.dto.response.gpt.GptResponseDto;
import com.tapioca.BE.application.prompt.GptPrompt;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.model.Mcp;
import com.tapioca.BE.domain.port.in.usecase.gpt.GptUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GptService implements GptUseCase {

    @Value("${mcp.server.url}")
    private String mcpUrl;

    private final RestTemplate restTemplate;
    private final McpMapper mcpMapper;
    private final OpenAiChatModel openAiChatModel;

    @Override
    public ResponseEntity<GptResponseDto> gptRequest(GptRequestDto gptRequestDto) {
        String prompt = makePrompt(gptRequestDto.userRequest());

        String gptResult = openAiChatModel.call(prompt);
        ObjectMapper mapper = new ObjectMapper();
        GptResponseDto responseDto;
        try{
            responseDto = mapper.readValue(gptResult, GptResponseDto.class);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_MAPPING_VALUE);
        }
        return sendRequestToMcp(responseDto);
    }

    public String makePrompt(String userRequest){
        String prompt = GptPrompt.MCP_PROMPT;
        return prompt.replace("${user_request}",userRequest);
    }

    public ResponseEntity<GptResponseDto> sendRequestToMcp(GptResponseDto gptResponseDto){
        Mcp mcp=mcpMapper.toDomain(gptResponseDto);

        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = "{\"type\" : \"" +mcp.getType()+"\"}";

        HttpEntity<String> entity=new HttpEntity<>(json,headers);
        return restTemplate.postForEntity(mcpUrl,entity,GptResponseDto.class);
    }
}

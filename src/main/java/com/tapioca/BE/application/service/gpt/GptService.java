package com.tapioca.BE.application.service.gpt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tapioca.BE.adapter.out.entity.BackEntity;
import com.tapioca.BE.application.dto.request.gpt.GptRequestDto;
import com.tapioca.BE.application.prompt.GptResultPrompt;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.port.in.usecase.gpt.GptUseCase;
import com.tapioca.BE.domain.port.out.repository.backend.BackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GptService implements GptUseCase {

    @Value("${mcp.server.url}")
    private String mcpUrl;

    private final RestTemplate restTemplate;
    private final OpenAiChatModel openAiChatModel;
    private final BackRepository backRepository;

    @Override
    public Object gptRequest(GptRequestDto gptRequestDto,UUID teamId,String authorizationHeader) {
        BackEntity backEntity = backRepository.findByTeamEntity_Id(teamId);
        String prompt = makePrompt(gptRequestDto.userRequest(),backEntity.getEc2Url());
        String gptResult = openAiChatModel.call(prompt);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode gptJson;
        try{
            gptJson = mapper.readValue(gptResult, JsonNode.class);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_MAPPING_VALUE);
        }

        if(gptJson instanceof ObjectNode){
            ((ObjectNode) gptJson).put("auth", authorizationHeader);
        }

        return sendRequestToMcp(gptJson,teamId);
    }

    // 프롬프트에 userRequest를 넣는 메서드
    public String makePrompt(String userRequest,String ec2Url){
        String prompt = GptResultPrompt.MCP_PROMPT;
        return prompt.replace("${user_request}",userRequest).replace("${ec2_url}",ec2Url);
    }

    // mcp 서버로 json을 보내는 메서드
    public Object sendRequestToMcp(JsonNode gptJson, UUID teamId){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<JsonNode> entity=new HttpEntity<>(gptJson,headers);
        return restTemplate.postForEntity(mcpUrl,entity,Object.class).getBody();
    }
}

package com.tapioca.BE.application.service.gptWork;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tapioca.BE.adapter.out.entity.project.BackEntity;
import com.tapioca.BE.adapter.out.mapper.BackEndMapper;
import com.tapioca.BE.application.service.gptCommon.MakePromptService;
import com.tapioca.BE.application.service.gptCommon.SendToMcpService;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.model.project.BackEnd;
import com.tapioca.BE.domain.port.in.usecase.gpt.TrafficTestUseCase;
import com.tapioca.BE.domain.port.out.repository.backend.BackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrafficTestService implements TrafficTestUseCase {

    private final SendToMcpService sendToMcpService;
    private final MakePromptService makePromptService;
    private final BackRepository backRepository;
    private final BackEndMapper backEndMapper;
    private final OpenAiChatModel openAiChatModel;

    @Override
    public Object trafficTest(
            String type,String userInput, UUID teamId
    ){
        BackEntity backEntity = backRepository.findByTeamEntity_Id(teamId);
        BackEnd backEnd = backEndMapper.toDomain(backEntity);

        String prompt = makePromptService.makeResultPrompt(type,userInput,backEnd.getEc2Url(),backEnd.getLoginPath());
        String result = openAiChatModel.call(prompt);
        System.out.println(result);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode resultJson;
        try{
            resultJson = mapper.readValue(result, JsonNode.class);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_MAPPING_VALUE);
        }
        if(resultJson.has("error")){
            throw new CustomException(ErrorCode.NOT_FOUND_RESULT);
        }

        return sendToMcpService.sendRequestToMcp(resultJson);
    }
}

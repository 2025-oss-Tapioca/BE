package com.tapioca.BE.application.service.gptTpye;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tapioca.BE.adapter.out.mapper.UserInputMapper;
import com.tapioca.BE.application.dto.request.gpt.UserInputRequestDto;
import com.tapioca.BE.application.service.gptCommon.MakePromptService;
import com.tapioca.BE.application.service.gptCommon.SendToMcpService;
import com.tapioca.BE.config.exception.CustomException;
import com.tapioca.BE.config.exception.ErrorCode;
import com.tapioca.BE.domain.model.user.UserInput;
import com.tapioca.BE.domain.port.in.usecase.gpt.GptTypeUseCase;
import com.tapioca.BE.domain.port.in.usecase.gpt.TrafficTestUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GptTypeService implements GptTypeUseCase {


    private final OpenAiChatModel openAiChatModel;
    private final MakePromptService makePromptService;
    private final SendToMcpService sendToMcpService;
    private final UserInputMapper userInputMapper;
    private final TrafficTestUseCase trafficTestUseCase;

    @Override
    public Object gptTypeRequest(
            UserInputRequestDto userInputRequestDto,
            UUID teamId
    ) {
        UserInput userInput = userInputMapper.toDomain(userInputRequestDto);

        String prompt = makePromptService.makeTypePrompt(userInput.getUserInput());
        String gptResult = openAiChatModel.call(prompt);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode gptJson;

        try{
            gptJson = mapper.readValue(gptResult, JsonNode.class);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_MAPPING_VALUE);
        }

        if(gptJson.has("error")){
            throw new CustomException(ErrorCode.NOT_FOUND_TYPE);
        }

        String type = gptJson.get("type").asText();
        switch (type){
            case "traffic_test":
                return trafficTestUseCase.trafficTest(type,userInput.getUserInput(),teamId);
        }

        return sendToMcpService.sendRequestToMcp(gptJson);
    }
}

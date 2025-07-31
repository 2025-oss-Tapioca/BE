package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.application.dto.response.gpt.GptResponseDto;
import com.tapioca.BE.domain.model.Mcp;
import org.springframework.stereotype.Component;

@Component
public class McpMapper {
    public Mcp toDomain(GptResponseDto gptResponseDto){
        return new Mcp(gptResponseDto.type());
    }
}

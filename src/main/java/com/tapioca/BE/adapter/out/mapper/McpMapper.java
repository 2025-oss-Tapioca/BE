package com.tapioca.BE.adapter.out.mapper;

import com.tapioca.BE.application.dto.request.mcp.McpRequestDto;
import com.tapioca.BE.domain.model.Mcp;
import org.springframework.stereotype.Component;

@Component
public class McpMapper {
    public Mcp toDomain(McpRequestDto mcpRequestDto){
        return new Mcp(mcpRequestDto.userRequest());
    }
}

package com.tapioca.BE.adapter.out.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.tapioca.BE.domain.model.Mcp;
import org.springframework.stereotype.Component;

@Component
public class McpMapper {
    public Mcp toDomain(JsonNode json){
        return new Mcp(json);
    }
}

package com.tapioca.BE.application.dto.request.mcp;

import com.fasterxml.jackson.databind.JsonNode;

public record McpRequestDto(
        String type,
        JsonNode json
) {
}

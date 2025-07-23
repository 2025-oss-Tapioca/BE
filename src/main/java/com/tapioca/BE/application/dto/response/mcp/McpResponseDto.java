package com.tapioca.BE.application.dto.response.mcp;

import com.fasterxml.jackson.databind.JsonNode;

public record McpResponseDto(
    String type,
    JsonNode data
) {
}

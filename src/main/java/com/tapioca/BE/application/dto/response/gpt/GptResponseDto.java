package com.tapioca.BE.application.dto.response.gpt;

import com.fasterxml.jackson.databind.JsonNode;

public record GptResponseDto(
        JsonNode json
) {
}
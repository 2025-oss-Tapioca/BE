package com.tapioca.BE.application.dto.response.erd;

import java.util.List;
import java.util.UUID;

public record DiagramResponseDto(
        UUID id,
        String name,
        List<AttributeResponseDto> attributes
) {}

package com.tapioca.BE.application.dto.response.erd;

import java.util.List;
import java.util.UUID;

public record ErdResponseDto(
        UUID id,
        UUID teamId,
        List<DiagramResponseDto> diagrams
) {}

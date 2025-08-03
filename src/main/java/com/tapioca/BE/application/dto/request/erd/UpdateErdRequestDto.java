package com.tapioca.BE.application.dto.request.erd;

import java.util.List;
import java.util.UUID;

public record UpdateErdRequestDto(
        String name,
        List<DiagramRequestDto> diagrams,
        List<AttributeLinkRequestDto> attributeLinks
) {
    public record DiagramRequestDto(
            String clientId,
            UUID diagramId,
            String diagramName,
            List<AttributeRequestDto> attributes
    ) {}

    public record AttributeRequestDto(
            String clientId,
            UUID attributeId,
            String attributeName,
            String attributeType,
            Integer varcharLength,
            boolean primaryKey,
            boolean foreignKey
    ) {}

    public record AttributeLinkRequestDto(
            String clientId,
            UUID attributeLinkId,
            String fromClientId,
            String toClientId,
            String linkType
    ) {}
}

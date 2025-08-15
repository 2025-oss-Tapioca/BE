package com.tapioca.BE.application.dto.request.erd;

import java.util.List;

public record UpdateErdRequestDto(
        List<DiagramRequestDto> diagrams,
        List<AttributeLinkRequestDto> attributeLinks
) {
    public record DiagramRequestDto(
            String diagramId,
            String diagramName,
            int diagramPosX,
            int diagramPosY,
            List<AttributeRequestDto> attributes
    ) {}

    public record AttributeRequestDto(
            String attributeId,
            String attributeName,
            String attributeType,
            Integer varcharLength,
            boolean primaryKey,
            boolean foreignKey
    ) {}

    public record AttributeLinkRequestDto(
            String fromClientId,
            String toClientId,
            String linkType
    ) {}
}
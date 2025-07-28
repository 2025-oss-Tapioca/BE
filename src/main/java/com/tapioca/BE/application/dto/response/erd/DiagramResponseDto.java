package com.tapioca.BE.application.dto.response.erd;

import com.tapioca.BE.adapter.out.entity.DiagramEntity;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record DiagramResponseDto(
        UUID id,
        String name,
        List<AttributeResponseDto> attributes
) {
    public static DiagramResponseDto of(DiagramEntity diagramEntity) {
        List<AttributeResponseDto> attributeList = diagramEntity.getAttributes().stream().map(AttributeResponseDto::of).toList();
        return new DiagramResponseDto(
                diagramEntity.getId(),
                diagramEntity.getName(),
                attributeList
        );
    }
}

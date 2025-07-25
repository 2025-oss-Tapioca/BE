package com.tapioca.BE.application.dto.response.erd;

import com.tapioca.BE.domain.model.Erd;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record ErdResponseDto(
        UUID id,
        UUID teamId,
        String name,
        List<DiagramResponseDto> diagrams
) {
    public static ErdResponseDto from(Erd erd) {
        List<DiagramResponseDto> ds = erd.getDiagrams().stream()
                .map(d -> new DiagramResponseDto(
                        d.getId(),
                        d.getName(),
                        d.getAttributes().stream()
                                .map(a -> new AttributeResponseDto(
                                        a.getId(),
                                        a.getName(),
                                        a.getAttributeType().name(),
                                        a.getLength(),
                                        a.getIsPrimaryKey(),
                                        a.getIsForeignKey()
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return new ErdResponseDto(
                erd.getId(),
                erd.getTeamId(),
                erd.getName(),
                ds
        );
    }

    public record DiagramResponseDto(
            UUID diagramId,
            String name,
            List<AttributeResponseDto> attributes
    ) {}

    public record AttributeResponseDto(
            UUID attributeId,
            String name,
            String attributeType,
            Integer length,
            Boolean primaryKey,
            Boolean foreignKey
    ) {}
}

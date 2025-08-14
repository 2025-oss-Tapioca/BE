package com.tapioca.BE.application.dto.response.erd;

import com.tapioca.BE.adapter.out.entity.erd.AttributeEntity;
import com.tapioca.BE.adapter.out.entity.erd.AttributeLinkEntity;
import com.tapioca.BE.adapter.out.entity.erd.DiagramEntity;
import com.tapioca.BE.adapter.out.entity.erd.ErdEntity;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record ErdResponseDto(
        UUID erdId,
        List<DiagramResponseDto> diagrams,
        List<AttributeLinkResponseDto> attributeLinks
) {
    public static ErdResponseDto from(ErdEntity erdEntity) {
        List<DiagramResponseDto> diagramDtos = erdEntity.getDiagrams().stream()
                .map(DiagramResponseDto::from)
                .collect(Collectors.toList());

        List<AttributeLinkResponseDto> linkDtos = erdEntity.getAttributeLinks().stream()
                .map(AttributeLinkResponseDto::from)
                .collect(Collectors.toList());

        return new ErdResponseDto(
                erdEntity.getId(),
                diagramDtos,
                linkDtos
        );
    }

    public record DiagramResponseDto(
            UUID diagramId,
            String diagramName,
            List<AttributeResponseDto> attributes
    ) {
        public static DiagramResponseDto from(DiagramEntity diagramEntity) {
            List<AttributeResponseDto> attributes = diagramEntity.getAttributes().stream()
                    .map(AttributeResponseDto::from)
                    .collect(Collectors.toList());
            return new DiagramResponseDto(diagramEntity.getId(), diagramEntity.getName(), attributes);
        }
    }

    public record AttributeResponseDto(
            UUID attributeId,
            String attributeName,
            String attributeType,
            Integer varcharLength,
            boolean primaryKey,
            boolean foreignKey
    ) {
        public static AttributeResponseDto from(AttributeEntity a) {
            return new AttributeResponseDto(
                    a.getId(),
                    a.getName(),
                    a.getAttributeType().name(),
                    a.getLength(),
                    a.isPrimaryKey(),
                    a.isForeignKey()
            );
        }
    }

    public record AttributeLinkResponseDto(
            UUID linkId,
            UUID fromAttributeId,
            UUID toAttributeId,
            String linkType
    ){
        public static AttributeLinkResponseDto from(AttributeLinkEntity attributeLinkEntity) {
            return new AttributeLinkResponseDto(
                    attributeLinkEntity.getId(),
                    attributeLinkEntity.getFromAttribute().getId(),
                    attributeLinkEntity.getToAttribute().getId(),
                    attributeLinkEntity.getLinkType().name()
            );
        }
    }
}

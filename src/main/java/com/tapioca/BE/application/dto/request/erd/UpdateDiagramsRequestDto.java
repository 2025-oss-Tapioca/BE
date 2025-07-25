package com.tapioca.BE.application.dto.request.erd;

import com.tapioca.BE.application.command.erd.UpdateDiagramsCommand;
import com.tapioca.BE.application.command.erd.UpdateDiagramsCommand.*;
import com.tapioca.BE.domain.model.type.AttributeType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record UpdateDiagramsRequestDto(
        @NotNull(message = "diagrams 목록은 필수입니다.")
        @Valid
        List<DiagramRequestDto> diagrams
) {
    public UpdateDiagramsCommand toCommand(UUID userId) {
        List<DiagramInfo> diagramInfos = new ArrayList<>();

        for (DiagramRequestDto d : diagrams) {
            List<AttributeInfo> attributeInfos = new ArrayList<>();

            if (d.attributes() != null) {
                for (AttributeRequestDto a : d.attributes()) {
                    attributeInfos.add(new AttributeInfo(
                            a.attributeId(),
                            a.name(),
                            a.attributeType().name(),  // enum → String
                            a.length(),
                            a.primaryKey(),
                            a.foreignKey()
                    ));
                }
            }

            diagramInfos.add(new DiagramInfo(
                    d.diagramId(),
                    d.name(),
                    attributeInfos
            ));
        }

        return new UpdateDiagramsCommand(userId, diagramInfos);}

    public static record DiagramRequestDto(
            UUID diagramId,
            @NotBlank(message = "다이어그램 이름은 필수입니다.")
            String name,
            @Valid
            List<AttributeRequestDto> attributes
    ) {}

    public static record AttributeRequestDto(
            UUID attributeId,
            @NotBlank(message = "속성 이름은 필수입니다.")
            String name,
            @NotNull(message = "속성 타입은 필수입니다.")
            AttributeType attributeType,
            Integer length,
            @NotNull(message = "PK 여부는 필수입니다.")
            Boolean primaryKey,
            @NotNull(message = "FK 여부는 필수입니다.")
            Boolean foreignKey
    ) {}
}

package com.tapioca.BE.application.dto.request.erd;


import com.tapioca.BE.application.command.erd.CreateAttributeCommand;
import com.tapioca.BE.application.command.erd.CreateDiagramCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateDiagramRequestDto(
        @NotNull(message = "ERD id 필수")
        UUID erdId,

        @NotBlank(message = "테이블 이름 필수")
        String name,

        List<AttributeRequestDto> attributes
) {

    public List<CreateAttributeCommand> toAttributeCommands() {
        List<CreateAttributeCommand> attributeList;
        if (attributes != null) {
            attributeList = attributes.stream()
                    .map(AttributeRequestDto::toCommand)
                    .toList();
        }
        else {
            attributeList = List.of();
        };

        return attributeList;
    }

    public CreateDiagramCommand toCommand() {
        return new CreateDiagramCommand(erdId, name, toAttributeCommands());
    }
}

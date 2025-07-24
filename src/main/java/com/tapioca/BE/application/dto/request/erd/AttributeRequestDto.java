package com.tapioca.BE.application.dto.request.erd;

import com.tapioca.BE.application.command.erd.CreateAttributeCommand;
import jakarta.validation.constraints.*;

public record AttributeRequestDto(
        @NotBlank(message = "속성 이름은 필수입니다.")
        String name,

        @NotNull(message = "속성 타입은 필수입니다.")
        com.tapioca.BE.domain.model.type.AttributeType type,

        Integer length,

        @NotNull(message = "PK 여부를 지정하세요.")
        Boolean isPk,

        @NotNull(message = "FK 여부를 지정하세요.")
        Boolean isFk
) {
    public CreateAttributeCommand toCommand() {
        return new CreateAttributeCommand(name, type, length, isPk, isFk);
    }
}

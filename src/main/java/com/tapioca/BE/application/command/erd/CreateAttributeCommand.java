package com.tapioca.BE.application.command.erd;

import com.tapioca.BE.domain.model.type.AttributeType;

public record CreateAttributeCommand(
        String name,
        AttributeType type,
        Integer length,
        Boolean isPk,
        Boolean isFk) {
}

package com.tapioca.BE.application.dto.response.erd;

import com.tapioca.BE.domain.model.type.AttributeType;

import java.util.UUID;

public record AttributeResponseDto(
        UUID id,
        String name,
        AttributeType attributeType,
        Integer length,
        boolean primaryKey,
        boolean foreignKey
) {}

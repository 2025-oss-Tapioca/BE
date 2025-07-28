package com.tapioca.BE.application.dto.response.erd;

import com.tapioca.BE.adapter.out.entity.AttributeEntity;

import java.util.UUID;

public record AttributeResponseDto(
        UUID id,
        String name,
        String type,
        Integer length,
        boolean isPk,
        boolean isFk
) {
    public static AttributeResponseDto of(AttributeEntity attributeEntity) {
        return new AttributeResponseDto(
                attributeEntity.getId(),
                attributeEntity.getName(),
                attributeEntity.getAttributeType().name(),
                attributeEntity.getLength(),
                attributeEntity.isPrimaryKey(),
                attributeEntity.isForeignKey()
        );
    }
}

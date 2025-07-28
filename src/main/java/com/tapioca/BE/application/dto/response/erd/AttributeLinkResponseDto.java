package com.tapioca.BE.application.dto.response.erd;

import com.tapioca.BE.adapter.out.entity.AttributeLinkEntity;

import java.util.UUID;

public record AttributeLinkResponseDto(
        UUID id,
        UUID fromId,
        UUID toId,
        String linkType
) {
    public static AttributeLinkResponseDto of(AttributeLinkEntity attributeLinkEntity) {
        return new AttributeLinkResponseDto(
                attributeLinkEntity.getId(),
                attributeLinkEntity.getFromAttribute().getId(),
                attributeLinkEntity.getToAttribute().getId(),
                attributeLinkEntity.getLinkType().name()
        );
    }
}

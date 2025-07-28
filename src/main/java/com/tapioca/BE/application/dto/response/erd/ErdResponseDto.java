package com.tapioca.BE.application.dto.response.erd;

import com.tapioca.BE.adapter.out.entity.AttributeLinkEntity;
import com.tapioca.BE.adapter.out.entity.ErdEntity;

import java.util.List;
import java.util.UUID;

public record ErdResponseDto(
        UUID id,
        String name,
        List<DiagramResponseDto> diagrams,
        List<AttributeLinkResponseDto> attributeLinks
) {
    public static ErdResponseDto of(ErdEntity erdEntity, List<AttributeLinkEntity> attributeLinks) {
        List<DiagramResponseDto> diagrams = erdEntity.getDiagrams().stream().map(DiagramResponseDto::of).toList();
        List<AttributeLinkResponseDto> attributeLinkList = attributeLinks.stream()
                .map(AttributeLinkResponseDto::of)
                .toList();
        return new ErdResponseDto(erdEntity.getId(), erdEntity.getName(), diagrams, attributeLinkList);
    }
}

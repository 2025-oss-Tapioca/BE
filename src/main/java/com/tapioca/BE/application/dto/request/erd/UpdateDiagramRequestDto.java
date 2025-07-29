package com.tapioca.BE.application.dto.request.erd;

import java.util.List;

public record UpdateDiagramRequestDto(
        String name,
        List<UpdateAttributeRequestDto> attributes
) {
    public String getName() {
        return name;
    }

    public List<UpdateAttributeRequestDto> getAttributes() {
        return attributes;
    }
}

package com.tapioca.BE.application.dto.request.erd;

import java.util.List;

public record UpdateErdRequestDto(
        String name,
        List<UpdateDiagramRequestDto> diagrams,
        List<UpdateAttributeLinkRequestDto> attributeLinks
) {
    public List<UpdateDiagramRequestDto> getDiagrams() {
        return diagrams;
    }

    public List<UpdateAttributeLinkRequestDto> getAttributeLinks() {
        return attributeLinks;
    }
}

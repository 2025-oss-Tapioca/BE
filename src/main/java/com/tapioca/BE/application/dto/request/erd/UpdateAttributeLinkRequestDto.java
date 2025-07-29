package com.tapioca.BE.application.dto.request.erd;

public record UpdateAttributeLinkRequestDto(
        String fromDiagram,
        String fromAttribute,
        String toDiagram,
        String toAttribute,
        String linkType
) {
    public String fromDiagram() {
        return fromDiagram;
    }

    public String fromAttribute() {
        return fromAttribute;
    }

    public String toDiagram() {
        return toDiagram;
    }

    public String toAttribute() {
        return toAttribute;
    }

    public String getLinkType() {
        return linkType;
    }

}

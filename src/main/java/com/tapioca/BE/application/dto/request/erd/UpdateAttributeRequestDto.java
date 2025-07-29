package com.tapioca.BE.application.dto.request.erd;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateAttributeRequestDto(
   String name,
   String attributeType,
   Integer length,
   boolean isPK,
   boolean isFK
) {
    public String getName() {
        return name;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public Integer getLength() {
        return length;
    }
}

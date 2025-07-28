package com.tapioca.BE.application.dto.request.erd;

public record UpdateAttributeRequestDto(
   String name,
   String attributeType,
   Integer length,
   Boolean isPK,
   Boolean isFK
) {}

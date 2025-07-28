package com.tapioca.BE.application.dto.request.erd;

import java.util.List;

public record UpdateDiagramRequestDto(
        String name,
        List<UpdateAttributeRequestDto> attributes
) {}

package com.tapioca.BE.application.dto.request.erd;

import java.util.UUID;

public record UpdateAttributeLinkRequestDto(
        UUID fromAttributeId,
        UUID toAttributeId,
        String linkType
) {}

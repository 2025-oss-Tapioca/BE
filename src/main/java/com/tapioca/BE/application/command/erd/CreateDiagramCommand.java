package com.tapioca.BE.application.command.erd;

import java.util.List;
import java.util.UUID;

public record CreateDiagramCommand(
        UUID userId,
        String name,
        List<CreateAttributeCommand> attributes
) {
}

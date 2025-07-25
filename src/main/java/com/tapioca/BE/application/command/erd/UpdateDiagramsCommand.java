package com.tapioca.BE.application.command.erd;


import com.tapioca.BE.domain.model.Diagram;

import java.util.List;
import java.util.UUID;

public record UpdateDiagramsCommand(
        UUID userId,
        List<UpdateDiagramsCommand.DiagramInfo> diagrams
) {
    public List<DiagramInfo> getDiagramInfos() {
        return diagrams;
    }

    public static record DiagramInfo(
            UUID diagramId,
            String name,
            List<UpdateDiagramsCommand.AttributeInfo> attributes
    ) {}

    public static record AttributeInfo(
            UUID attributeId,
            String name,
            String type,
            Integer length,
            Boolean primaryKey,
            Boolean foreignKey
    ) {
        public String getAttributeType() {
            return this.type;
        }
    }
}

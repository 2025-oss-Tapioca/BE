package com.tapioca.BE.domain.model;

import com.tapioca.BE.domain.model.type.AttributeType;
import com.tapioca.BE.domain.model.type.LinkType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Attribute {
    private final UUID id;
    private final UUID diagramId;
    private final String name;
    private final AttributeType attributeType;
    private final Integer length;
    private final boolean isPrimaryKey;
    private final boolean isForeignKey;

    public Attribute(UUID id, UUID diagramId, String name, AttributeType attributeType, Integer length,  boolean isPrimaryKey, boolean isForeignKey) {
        this.id = id;
        this.diagramId = diagramId;
        this.name = name;
        this.attributeType = attributeType;
        this.length = length;
        this.isPrimaryKey = isPrimaryKey;
        this.isForeignKey = isForeignKey;
    }
}
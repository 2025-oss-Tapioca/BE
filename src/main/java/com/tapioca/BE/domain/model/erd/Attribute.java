package com.tapioca.BE.domain.model.erd;

import com.tapioca.BE.domain.model.enumType.AttributeType;
import lombok.Getter;

@Getter
public class Attribute {
    private final String id;
    private final String diagramId;
    private final String name;
    private final AttributeType attributeType;
    private final Integer length;
    private final boolean isPrimaryKey;
    private final boolean isForeignKey;

    public Attribute(String id, String diagramId, String name, AttributeType attributeType, Integer length,  boolean isPrimaryKey, boolean isForeignKey) {
        this.id = id;
        this.diagramId = diagramId;
        this.name = name;
        this.attributeType = attributeType;
        this.length = length;
        this.isPrimaryKey = isPrimaryKey;
        this.isForeignKey = isForeignKey;
    }
}
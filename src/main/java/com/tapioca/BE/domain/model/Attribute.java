package com.tapioca.BE.domain.model;

import com.tapioca.BE.domain.model.type.AttributeType;
import com.tapioca.BE.domain.model.type.LinkType;

import java.util.UUID;

public class Attribute {
    private final UUID id;
    private final String name;
    private final AttributeType attributeType;
    private final Integer length;
    private final boolean isPrimaryKey;
    private final boolean isForeignKey;

    public Attribute(UUID id, String name, AttributeType attributeType, Integer length,  boolean isPrimaryKey, boolean isForeignKey) {
        this.id = id;
        this.name = name;
        this.attributeType = attributeType;
        this.length = length;
        this.isPrimaryKey = isPrimaryKey;
        this.isForeignKey = isForeignKey;
    }

    public UUID getId() {
        return id;
    }

    public boolean isVarchar() {
        return attributeType == AttributeType.VARCHAR;
    }

    public Integer getLength() {
        if(isVarchar()) {
            return length;
        }
        else {
            return 0;
        }
    }

    public String getName() {
        return name;
    }

    public AttributeType getAttributeType() {
        return attributeType;
    }

    public boolean getIsPrimaryKey() {
        return isPrimaryKey;
    }

    public boolean getIsForeignKey() {
        return isForeignKey;
    }
}
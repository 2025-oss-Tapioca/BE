package com.tapioca.BE.domain.model.erd;


import com.tapioca.BE.domain.model.enumType.LinkType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class AttributeLink {
    private final String id;
    private final String fromAttributeId;
    private final String toAttributeId;
    private final LinkType linkType;
    private final String sourceCard;
    private final String targetCard;
    private final boolean identifying;

    public AttributeLink(
            String id, String fromAttributeId,
            String toAttributeId, LinkType linkType,
            String sourceCard, String targetCard, boolean identifying) {
        this.id = id;
        this.fromAttributeId = fromAttributeId;
        this.toAttributeId = toAttributeId;
        this.linkType = linkType;
        this.sourceCard = sourceCard;
        this.targetCard = targetCard;
        this.identifying = identifying;
    }
}


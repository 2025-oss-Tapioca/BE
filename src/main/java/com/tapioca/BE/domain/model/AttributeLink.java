package com.tapioca.BE.domain.model;


import com.tapioca.BE.domain.model.type.LinkType;
import java.util.UUID;

public class AttributeLink {
    private final UUID id;
    private final UUID fromAttributeId;
    private final UUID toAttributeId;
    private final LinkType linkType;

    public AttributeLink(UUID id, UUID fromAttributeId, UUID toAttributeId, LinkType linkType) {
        this.id = id;
        this.fromAttributeId = fromAttributeId;
        this.toAttributeId = toAttributeId;
        this.linkType = linkType;
    }
}


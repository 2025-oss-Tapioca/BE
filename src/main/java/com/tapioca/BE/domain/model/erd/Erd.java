package com.tapioca.BE.domain.model.erd;
import lombok.Getter;

import java.util.List;

@Getter
public class Erd {
    List<Diagram> diagrams;
    List<Attribute> attributes;
    List<AttributeLink> attributeLinks;

    public Erd(List<Diagram> diagrams, List<Attribute> attributes, List<AttributeLink> attributeLinks) {
        this.diagrams = List.copyOf(diagrams);
        this.attributes = List.copyOf(attributes);
        this.attributeLinks = List.copyOf(attributeLinks);
    }
}

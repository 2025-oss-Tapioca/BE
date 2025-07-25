package com.tapioca.BE.domain.model;

import com.tapioca.BE.config.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Diagram {
    private final UUID id;
    private final String name;
    private final List<Attribute>  attributes = new ArrayList<>();

    public Diagram(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public List<Attribute> getAttributes() {
        return List.copyOf(attributes);
    }

    public static Diagram of(UUID id, String name, List<Attribute> attributes) {
        Diagram diagram = new Diagram(id, name);
        for (Attribute a : attributes) {
            diagram.addAttribute(a);
        }
        return diagram;
    }

    public void addAttribute(Attribute attribute) {
        attributes.add(attribute);
    }

    public void removeAttribute(UUID attributeId) {
        attributes.removeIf(a -> a.getId().equals(attributeId));
    }

}

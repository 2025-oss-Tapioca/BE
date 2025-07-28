package com.tapioca.BE.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Erd {
    private final UUID id;
    private final String name;
    private final List<Diagram> diagrams = new ArrayList<>();

    public Erd(UUID id, String name) {
        this.id   = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Diagram> getDiagrams() {
        return List.copyOf(diagrams);
    }

    public void addDiagram(Diagram diagram) {
        diagrams.add(diagram);
    }

    public void removeDiagram(UUID diagramId) {
        diagrams.removeIf(d -> d.getId().equals(diagramId));
    }
}

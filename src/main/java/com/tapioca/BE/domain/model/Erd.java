package com.tapioca.BE.domain.model;

import com.tapioca.BE.config.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Erd {
    private final UUID id;
    private final UUID teamId;
    private final String name;
    private final List<Diagram> diagrams = new ArrayList<>();

    public Erd(UUID id, UUID teamId, String name) {
        this.id = id;
        this.teamId = teamId;
        this.name = name;
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

    public UUID getId() {
        return id;
    }

    public UUID getTeamId() {
        return teamId;
    }
    public String getName() {
        return name;
    }
}
package com.tapioca.BE.domain.model;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Diagram {
    private final UUID id;
    private final UUID erdId;
    private final String name;

    public Diagram(UUID id, UUID erdId, String name) {
        this.id = id;
        this.erdId = erdId;
        this.name = name;
    }
}

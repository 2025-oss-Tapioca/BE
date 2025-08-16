package com.tapioca.BE.domain.model.erd;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Diagram {
    private final String id;
    private final String erdId;
    private final String name;
    private final int posX;
    private final int posY;

    public Diagram(String id, String erdId, String name, int posX, int posY) {
        this.id = id;
        this.erdId = erdId;
        this.name = name;
        this.posX = posX;
        this.posY = posY;
    }
}

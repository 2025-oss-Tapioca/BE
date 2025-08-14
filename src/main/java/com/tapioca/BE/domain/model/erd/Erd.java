package com.tapioca.BE.domain.model.erd;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Erd {
    private final UUID id;
    private final UUID teamId;

    public Erd(UUID id, UUID teamId) {
        this.id = id;
        this.teamId = teamId;
    }
}

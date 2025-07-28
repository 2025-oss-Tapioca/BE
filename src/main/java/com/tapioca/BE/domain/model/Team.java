package com.tapioca.BE.domain.model;

import java.util.UUID;

public class Team {
    private final UUID id;
    private final String name;
    private final String code;
    private final Erd erd;

    public Team(UUID id, String name, String code, Erd erd) {
        this.id   = id;
        this.name = name;
        this.code = code;
        this.erd  = erd;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public Erd getErd() {
        return erd;
    }
}

package com.tapioca.BE.domain.model;

import java.util.UUID;

public class Team {
    private final UUID id;
    private final String name;
    private final String code;

    public Team(UUID id, String name, String code){
        this.id=id;
        this.name=name;
        this.code=code;
    }

    // Team Service //
}

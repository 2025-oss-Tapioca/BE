package com.tapioca.BE.domain.model.user;

import lombok.Getter;

@Getter
public class Team {
    private final String name;
    private final String description;

    public Team(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

package com.tapioca.BE.domain.model;


import java.util.UUID;

public class User {
    private final UUID id;
    private final String email;
    private final String name;
    private final String userId;
    private final String password;

    public User(
            UUID id, String email,
            String name, String userId,
            String password
    ){
        this.id = id;
        this.email = email;
        this.userId = userId;
        this.name = name;
        this.password = password;
    }

    // User Service //
}

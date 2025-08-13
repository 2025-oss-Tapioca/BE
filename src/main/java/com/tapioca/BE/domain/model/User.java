package com.tapioca.BE.domain.model;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class User {
    private final UUID id;
    private final String email;
    private final String name;
    private final String userId;
    private String password;

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

    public User(
            UUID id,
            String name, String userId,
            String password
    ){
        this.id = id;
        this.email = null;
        this.userId = userId;
        this.name = name;
        this.password = password;
    }

    public void changePassword(String password){
        this.password = password;
    }
}

package com.tapioca.BE.domain.model.user;


import lombok.Getter;

import java.util.UUID;

@Getter
public class User {
    private final UUID id;
    private final String email;
    private final String name;
    private final String loginId;
    private String password;

    public User(
            UUID id, String email,
            String name, String loginId,
            String password
    ){
        this.id = id;
        this.email = email;
        this.loginId = loginId;
        this.name = name;
        this.password = password;
    }

    public User(
            UUID id,
            String loginId,
            String password
    ){
        this.id = id;
        this.email = null;
        this.name = null;
        this.loginId = loginId;
        this.password = password;
    }

    public void changePassword(String password){
        this.password = password;
    }
}

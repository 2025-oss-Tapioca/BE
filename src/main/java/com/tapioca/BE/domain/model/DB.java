package com.tapioca.BE.domain.model;

import java.util.UUID;

public class DB {
    private final UUID id;
    private final UUID teamId;
    private final String dbAddress;
    private final String dbUser;
    private final String password;

    public DB(
            UUID id, UUID teamId,
            String dbAddress, String dbUser,
            String password
    ){
        this.id=id;
        this.teamId=teamId;
        this.dbAddress=dbAddress;
        this.dbUser=dbUser;
        this.password=password;
    }

    // DB Service //
}

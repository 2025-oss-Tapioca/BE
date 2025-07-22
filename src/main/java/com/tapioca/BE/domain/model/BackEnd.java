package com.tapioca.BE.domain.model;

public class BackEnd {
    private final String ec2Host;
    private final String authToken;
    private final String os;
    private final String env;

    public BackEnd(
        String ec2Host, String authToken,
        String os, String env
    ){
        this.ec2Host=ec2Host;
        this.authToken=authToken;
        this.os=os;
        this.env=env;
    }
}

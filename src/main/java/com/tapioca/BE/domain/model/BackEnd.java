package com.tapioca.BE.domain.model;


import java.util.UUID;

public class BackEnd {
    private final String ec2Host;
    private final String ec2Url;
    private final String authToken;
    private final String os;
    private final String env;

    public BackEnd(
        String ec2Host, String authToken,
        String os, String env, String ec2Url
    ){
        this.ec2Host=ec2Host;
        this.authToken=authToken;
        this.os=os;
        this.env=env;
        this.ec2Url=ec2Url;
    }

    public String getEc2Url(){
        return ec2Url;
    }
}

package com.tapioca.BE.domain.model;


import java.util.UUID;

public class BackEnd {
    private final String loginPath;
    private final String ec2Url;
    private final String os;
    private final String env;

    public BackEnd(
        String loginPath,String os, String env, String ec2Url
    ){
        this.loginPath=loginPath;
        this.os=os;
        this.env=env;
        this.ec2Url=ec2Url;
    }

    public String getEc2Url(){
        return ec2Url;
    }
    public String getLoginPath() { return loginPath; }
}

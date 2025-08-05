package com.tapioca.BE.domain.model;


import java.util.UUID;

public class BackEnd {
    private final String loginPath;
    private final String ec2Url;
    private final String authToken;
    private final String os;
    private final String env;

    public BackEnd(
        String loginPath, String authToken,
        String os, String env, String ec2Url
    ){
        this.loginPath = loginPath;
        this.ec2Url = ec2Url;
        this.authToken = authToken;
        this.os = os;
        this.env = env;
    }

    public String getLoginPath() { return loginPath; }
    public String getEc2Url() { return ec2Url; }
    public String getAuthToken() { return authToken; }
    public String getOs() { return os; }
    public String getEnv() { return env; }
}

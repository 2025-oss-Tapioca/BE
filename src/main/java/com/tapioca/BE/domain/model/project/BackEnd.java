package com.tapioca.BE.domain.model.project;


public class BackEnd {
    private final String loginPath;
    private final String ec2Url;
    private final String os;
    private final String env;

    public BackEnd(
        String loginPath,
        String os, String env, String ec2Url
    ){
        this.loginPath = loginPath;
        this.ec2Url = ec2Url;
        this.os = os;
        this.env = env;
    }

    public String getLoginPath() { return loginPath; }
    public String getEc2Url() { return ec2Url; }
    public String getOs() { return os; }
    public String getEnv() { return env; }
}

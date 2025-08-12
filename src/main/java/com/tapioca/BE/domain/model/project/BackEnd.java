package com.tapioca.BE.domain.model.project;


public class BackEnd {
    private final String teamCode;
    private final String loginPath;
    private final String ec2Url;
    private final String authToken;
    private final String os;
    private final String env;

    public BackEnd(
        String teamCode, String loginPath,
        String ec2Url, String authToken,
        String os, String env
    ){
        this.teamCode = teamCode;
        this.loginPath = loginPath;
        this.ec2Url = ec2Url;
        this.authToken = authToken;
        this.os = os;
        this.env = env;
    }

    public String getTeamCode() { return teamCode; }
    public String getLoginPath() { return loginPath; }
    public String getEc2Url() { return ec2Url; }
    public String getAuthToken() { return authToken; }
    public String getOs() { return os; }
    public String getEnv() { return env; }
}

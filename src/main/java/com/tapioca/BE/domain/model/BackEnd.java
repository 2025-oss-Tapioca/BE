package com.tapioca.BE.domain.model;


import java.util.UUID;

public class BackEnd {
    private final UUID id;
    private final UUID teamId;
    private final String ec2Host;
    private final String ec2Url;
    private final String authToken;
    private final String os;
    private final String env;

    public BackEnd(
        UUID id, UUID teamId,
        String ec2Host, String authToken,
        String os, String env, String ec2Url
    ){
        this.id = id;
        this.teamId = teamId;
        this.ec2Host = ec2Host;
        this.ec2Url = ec2Url;
        this.authToken = authToken;
        this.os = os;
        this.env = env;
    }

    public UUID getId() { return id; }
    public UUID getTeamId() { return teamId; }
    public String getEc2Host() { return ec2Host; }
    public String getEc2Url() { return ec2Url; }
    public String getAuthToken() { return authToken; }
    public String getOs() { return os; }
    public String getEnv() { return env; }
}

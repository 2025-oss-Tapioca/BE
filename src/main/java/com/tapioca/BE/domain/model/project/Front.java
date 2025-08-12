package com.tapioca.BE.domain.model.project;

import java.util.UUID;

public class Front {
    private final String teamCode;
    private final String ec2Host;
    private final String authToken;
    private final String entryPoint;
    private final String os;
    private final String env;
    private final String protocol;

    public Front(
            String teamCode, String ec2Host,
            String authToken, String entryPoint,
            String os, String env, String protocol
    ) {
        this.teamCode = teamCode;
        this.ec2Host = ec2Host;
        this.authToken = authToken;
        this.entryPoint = entryPoint;
        this.os = os;
        this.env = env;
        this.protocol = protocol;
    }

    public String getTeamCode() { return teamCode; }
    public String getEc2Host() { return ec2Host; }
    public String getAuthToken() { return authToken; }
    public String getEntryPoint() { return entryPoint; }
    public String getOs() { return os; }
    public String getEnv() { return env; }
    public String getProtocol() { return protocol; }
}

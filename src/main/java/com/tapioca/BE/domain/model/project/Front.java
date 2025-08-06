package com.tapioca.BE.domain.model.project;

import java.util.UUID;

public class Front {
    private final String ec2Host;
    private final String entryPoint;
    private final String os;
    private final String env;
    private final String protocol;

    public Front(
            String ec2Host, String entryPoint,
            String os, String env, String protocol
    ) {
        this.ec2Host = ec2Host;
        this.entryPoint = entryPoint;
        this.os = os;
        this.env = env;
        this.protocol = protocol;
    }

    public String getEc2Host() { return ec2Host; }
    public String getEntryPoint() { return entryPoint; }
    public String getOs() { return os; }
    public String getEnv() { return env; }
    public String getProtocol() { return protocol; }
}

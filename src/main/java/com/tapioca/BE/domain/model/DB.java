package com.tapioca.BE.domain.model;

import java.util.UUID;

public class DB {
    private final UUID id;
    private final UUID teamId;
    private final String dbAddress;
    private final String dbUser;
    private final String password;
    private final String dbName;
    private final String dbPort;
    private final String rdsInstanceId;
    private final String awsRegion;
    private final String awsAccessKey;
    private final String awsSecretKey;


    public DB(
            UUID id, UUID teamId,
            String dbAddress, String dbUser,
            String password, String dbName, String dbPort,
            String rdsInstanceId, String awsRegion,
            String awsAccessKey, String awsSecretKey
    ){
        this.id=id;
        this.teamId=teamId;
        this.dbAddress=dbAddress;
        this.dbUser=dbUser;
        this.password=password;
        this.dbName=dbName;
        this.dbPort=dbPort;
        this.rdsInstanceId=rdsInstanceId;
        this.awsRegion=awsRegion;
        this.awsAccessKey=awsAccessKey;
        this.awsSecretKey=awsSecretKey;
    }

    public UUID getId() { return id; }
    public UUID getTeamId() { return teamId; }
    public String getDbAddress() { return dbAddress; }
    public String getDbUser() { return dbUser; }
    public String getPassword() { return password; }
    public String getDbName() { return dbName; }
    public String getDbPort() { return dbPort; }
    public String getRdsInstanceId() { return rdsInstanceId; }
    public String getAwsRegion() { return awsRegion; }
    public String getAwsAccessKey() { return awsAccessKey; }
    public String getAwsSecretKey() { return awsSecretKey; }

    // DB Service //
}

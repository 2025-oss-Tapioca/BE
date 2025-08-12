package com.tapioca.BE.domain.model.project;

import java.util.UUID;

public class DB {
    private final String teamCode;
    private final String dbAddress;
    private final String dbUser;
    private final String password;
    private final String dbName;
    private final String dbPort;
    private final String rdsInstanceId;
    private final String awsRegion;
    private final String roleArn;


    public DB(
            String teamCode, String dbAddress, String dbUser,
            String password, String dbName, String dbPort,
            String rdsInstanceId, String awsRegion, String roleArn
    ){
        this.teamCode = teamCode;
        this.dbAddress=dbAddress;
        this.dbUser=dbUser;
        this.password=password;
        this.dbName=dbName;
        this.dbPort=dbPort;
        this.rdsInstanceId=rdsInstanceId;
        this.awsRegion=awsRegion;
        this.roleArn=roleArn;
    }

    public String getTeamCode() { return teamCode; }
    public String getDbAddress() { return dbAddress; }
    public String getDbUser() { return dbUser; }
    public String getPassword() { return password; }
    public String getDbName() { return dbName; }
    public String getDbPort() { return dbPort; }
    public String getRdsInstanceId() { return rdsInstanceId; }
    public String getAwsRegion() { return awsRegion; }
    public String getRoleArn() { return roleArn; }

    // DB Service //
}

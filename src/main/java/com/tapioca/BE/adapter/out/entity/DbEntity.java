package com.tapioca.BE.adapter.out.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "db")
public class DbEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "db_id")
    private UUID id;

    @JoinColumn(name = "team_id")
    @OneToOne
    private TeamEntity teamEntity;

    @Column(name = "db_address")
    private String address;

    @Column(name = "db_user")
    private String user;

    @Column(name = "db_password")
    private String password;

    @Column(name = "db_name")
    private String name;

    @Column(name = "db_port")
    private String port;

    @Column(name = "db_rds_instance_id")
    private String rdsInstanceId;

    @Column(name = "db_aws_region")
    private String awsRegion;

    @Column(name = "db_aws_access_key")
    private String awsAccessKey;

    @Column(name = "db_aws_secret_key")
    private String awsSecretKey;
}

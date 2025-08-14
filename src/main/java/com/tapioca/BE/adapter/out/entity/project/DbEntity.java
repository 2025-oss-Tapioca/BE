package com.tapioca.BE.adapter.out.entity.project;

import com.tapioca.BE.adapter.out.entity.user.TeamEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "db")
public class DbEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "db_id")
    private UUID id;

    @JoinColumn(name = "team_id", nullable = false, unique = true)
    @OneToOne(fetch = FetchType.LAZY)
    private TeamEntity teamEntity;

    @Column(name = "db_address", nullable = false)
    private String address;

    @Column(name = "db_user", nullable = false)
    private String user;

    @Column(name = "db_password", nullable = false)
    private String password;

    @Column(name = "db_name", nullable = false)
    private String name;

    @Column(name = "db_port", nullable = false)
    private String port;

    @Column(name = "db_rds_instance_id", nullable = false)
    private String rdsInstanceId;

    @Column(name = "db_aws_region", nullable = false)
    private String awsRegion;

    @Column(name = "db_role_arn", nullable = false)
    private String roleArn;
}

package com.tapioca.BE.adapter.out.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "back")
public class BackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "back_id")
    private UUID id;

    @JoinColumn(name = "team_id")
    @OneToOne
    private TeamEntity teamEntity;

    @Column(name = "back_ec2_host")
    private String ec2Host;

    @Column(name = "back_ec2_url")
    private String ec2Url;

    @Column(name = "back_auth_token")
    private String authToken;

    @Column(name = "back_os")
    private String os;

    @Column(name = "back_env")
    private String env;
}

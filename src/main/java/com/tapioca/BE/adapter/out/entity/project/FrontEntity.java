package com.tapioca.BE.adapter.out.entity.project;

import com.tapioca.BE.adapter.out.entity.user.TeamEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "front")
public class FrontEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "front_id")
    private UUID id;

    @JoinColumn(name = "team_id")
    @OneToOne
    private TeamEntity teamEntity;

    @Column(name = "front_ec2_host")
    private String ec2Host;

    @Column(name = "front_auth_token")
    private String authToken;

    @Column(name = "front_entry_point")
    private String entryPoint;

    @Column(name = "front_os")
    private String os;

    @Column(name = "front_env")
    private String env;

    @Column(name = "front_protocol")
    private String protocol;
}

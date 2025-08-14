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
@Table(name = "front")
public class FrontEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "front_id")
    private UUID id;

    @JoinColumn(name = "team_id", nullable = false, unique = true)
    @OneToOne(fetch = FetchType.LAZY)
    private TeamEntity teamEntity;

    @Column(name = "front_ec2_host", nullable = false)
    private String ec2Host;

    @Column(name = "front_auth_token", nullable = false)
    private String authToken;

    @Column(name = "front_entry_point", nullable = false)
    private String entryPoint;

    @Column(name = "front_os", nullable = false)
    private String os;

    @Column(name = "front_env", nullable = false)
    private String env;

    @Column(name = "front_protocol", nullable = false)
    private String protocol;
}

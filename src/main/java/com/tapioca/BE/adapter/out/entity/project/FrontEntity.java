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

    @Column(name = "ec2Host")
    private String ec2Host;

    @Column(name = "entryPoint")
    private String entryPoint;

    @Column(name = "os")
    private String os;

    @Column(name = "env")
    private String env;

    @Column(name = "protocol")
    private String protocol;
}

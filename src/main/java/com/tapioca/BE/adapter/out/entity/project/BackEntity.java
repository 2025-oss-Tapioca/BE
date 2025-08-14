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
@Table(name = "back")
public class BackEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "back_id")
    private UUID id;

    @JoinColumn(name = "team_id", nullable = false, unique = true)
    @OneToOne(fetch = FetchType.LAZY)
    private TeamEntity teamEntity;

    @Column(name = "back_login_path", nullable = false)
    private String loginPath;

    @Column(name = "back_ec2_url", nullable = false)
    private String ec2Url;

    @Column(name = "back_auth_token", nullable = false)
    private String authToken;

    @Column(name = "back_os", nullable = false)
    private String os;

    @Column(name = "back_env", nullable = false)
    private String env;
}

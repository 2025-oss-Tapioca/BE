package com.tapioca.BE.adapter.out.entity.project;

import com.tapioca.BE.adapter.out.entity.user.TeamEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Entity
@Table(name="github_config")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GitHubEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="github_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TeamEntity teamEntity;

    @Column(name = "repo_url", nullable = false)
    private String repoUrl;

    @Column(name = "is_private", nullable = false)
    private boolean isPrivate;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "default_branch", nullable = false)
    private String defaultBranch;
}

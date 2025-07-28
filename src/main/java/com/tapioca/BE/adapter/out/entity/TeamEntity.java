package com.tapioca.BE.adapter.out.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "team")
public class TeamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "team_id")
    private UUID id;

    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ErdEntity erdEntity;

    @Column(name = "team_name")
    private String name;

    @Column(name = "team_code")
    private String code;

    public void setErd(ErdEntity erdEntity) {
        this.erdEntity = erdEntity;
    }
}

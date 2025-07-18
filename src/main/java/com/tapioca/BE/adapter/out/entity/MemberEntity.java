package com.tapioca.BE.adapter.out.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "member_id")
    private UUID id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity userEntity;

    @JoinColumn(name = "team_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TeamEntity teamEntity;

    @Column(name = "member_role")
    private String memberRole;
}

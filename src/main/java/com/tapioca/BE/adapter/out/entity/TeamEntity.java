package com.tapioca.BE.adapter.out.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Column(name = "team_name")
    private String name;

    @Column(name = "team_code")
    private String code;
}

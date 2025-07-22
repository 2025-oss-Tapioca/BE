package com.tapioca.BE.adapter.out.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "erd")
public class ErdEntity {
    @Id
    @Column(name = "erd_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(name = "team_id")
    private UUID teamId;

    @Column(name = "primary_key")
    private String primaryKey;

    @Column(name = "forigen_key")
    private String foreignKey;
}

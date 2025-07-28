package com.tapioca.BE.adapter.out.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
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

    @Column
    private UUID teamId;

    @Column(name = "erd_name")
    private String name;

    @OneToMany(mappedBy = "erd", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<DiagramEntity> diagrams = new ArrayList<>();

    public void addDiagram(DiagramEntity diagram) {
        diagrams.add(diagram);
        diagram.setErd(this);
    }

    public void removeDiagram(DiagramEntity diagram) {
        diagrams.remove(diagram);
        diagram.setErd(null);
    }
}

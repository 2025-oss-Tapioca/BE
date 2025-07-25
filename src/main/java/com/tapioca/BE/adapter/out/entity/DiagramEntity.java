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
@Table(name = "diagram")
public class DiagramEntity {
    @Id
    @Column(name = "diagram_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "diagram_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="erd_id", nullable = false)
    private ErdEntity erd;

    @OneToMany(mappedBy = "diagram", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<AttributeEntity> attributes = new ArrayList<>();

    public void setErd(ErdEntity erd) {
        this.erd = erd;
    }

    public void addAttribute(AttributeEntity attr) {
        attributes.add(attr);
        attr.setDiagram(this);
    }

    public void removeAttribute(AttributeEntity attr) {
        attributes.remove(attr);
        attr.setDiagram(null);
    }
}

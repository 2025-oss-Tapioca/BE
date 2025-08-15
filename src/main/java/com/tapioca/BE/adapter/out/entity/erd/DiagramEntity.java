package com.tapioca.BE.adapter.out.entity.erd;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.*;


@Entity
@Getter
@Setter
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

    @Column(name = "diagram_pos_x")
    private int posX;

    @Column(name = "diagram_pos_y")
    private int posY;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="erd_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ErdEntity erdEntity;

    @OneToMany(mappedBy = "diagram",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @Builder.Default
    private Set<AttributeEntity> attributes = new HashSet<>();

    public void addAttribute(AttributeEntity attribute) {
        this.attributes.add(attribute);
        attribute.setDiagram(this);
    }
}

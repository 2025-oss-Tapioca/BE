package com.tapioca.BE.adapter.out.entity.erd;

import com.tapioca.BE.adapter.out.entity.user.TeamEntity;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.util.*;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "erd")
public class ErdEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "erd_id")
    private UUID id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TeamEntity teamEntity;

    @OneToMany(mappedBy = "erdEntity", cascade = ALL, orphanRemoval = true)
    @OrderColumn(name = "diagram_order")
    @Builder.Default
    private List<DiagramEntity> diagrams = new ArrayList<>();

    @OneToMany(mappedBy = "erdEntity", cascade = ALL, orphanRemoval = true)
    @OrderColumn(name = "attr_link_order")
    @Builder.Default
    private List<AttributeLinkEntity> attributeLinks = new ArrayList<>();

    public void addDiagram(DiagramEntity diagram) {
        this.diagrams.add(diagram);
        diagram.setErdEntity(this);
    }

    public void addAttributeLink(AttributeLinkEntity attributeLink) {
        this.attributeLinks.add(attributeLink);
        attributeLink.setErdEntity(this);
    }
}

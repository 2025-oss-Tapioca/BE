package com.tapioca.BE.adapter.out.entity.erd;

import com.tapioca.BE.adapter.out.entity.user.TeamEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.*;

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

    @OneToMany(mappedBy = "erdEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @Builder.Default
    private Set<DiagramEntity> diagrams = new HashSet<>();

    @OneToMany(mappedBy = "erdEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @Builder.Default
    private Set<AttributeLinkEntity> attributeLinks = new HashSet<>();

    public void addDiagram(DiagramEntity diagram) {
        this.diagrams.add(diagram);
        diagram.setErdEntity(this);
    }

    public void addAttributeLink(AttributeLinkEntity attributeLink) {
        this.attributeLinks.add(attributeLink);
        attributeLink.setErdEntity(this);
    }
}

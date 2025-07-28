package com.tapioca.BE.adapter.out.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @Column(name = "erd_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "team_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_erd_team",
                    foreignKeyDefinition =
                            "FOREIGN KEY (team_id) REFERENCES team(team_id) ON DELETE CASCADE"
            )
    )
    private TeamEntity teamEntity;

    @OneToMany(mappedBy = "erd", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<DiagramEntity> diagrams = new ArrayList<>();

    public void setTeam(TeamEntity teamEntity) {
        this.teamEntity = teamEntity;
    }

    public void addDiagram(DiagramEntity diagram) {
        diagrams.add(diagram);
        diagram.setErd(this);
    }

    public void removeDiagram(DiagramEntity diagram) {
        diagrams.remove(diagram);
        diagram.setErd(null);
    }
}

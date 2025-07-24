package com.tapioca.BE.adapter.out.entity;

import com.tapioca.BE.domain.model.type.AttributeType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="attribute")
public class AttributeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="attribute_id")
    private UUID id;

    @Column(name = "attribute_name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "attribute_type")
    private AttributeType attributeType;

    @Column(name = "attribute_type_varchar_length")
    private Integer length;

    @Column(name = "is_pk", nullable = false)
    private boolean isPrimaryKey;

    @Column(name = "is_fk", nullable = false)
    private boolean isForeignKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="diagram_id", nullable = false)
    private DiagramEntity diagram;

    public void setDiagram(DiagramEntity diagram) {
        this.diagram = diagram;
    }
}

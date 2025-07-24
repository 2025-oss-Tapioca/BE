package com.tapioca.BE.adapter.out.entity;

import com.tapioca.BE.domain.model.type.LinkType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "attribute_link")
public class AttributeLinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="attribute_link_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="from_attribute", nullable = false)
    private AttributeEntity fromAttribute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="to_attribute", nullable = false)
    private AttributeEntity toAttribute;

    @Enumerated(EnumType.STRING)
    @Column(name="link_type")
    private LinkType linkType;

    public void setFromAttribute(AttributeEntity attr) {
        this.fromAttribute = attr;
    }

    public void setToAttribute(AttributeEntity attr) {
        this.toAttribute = attr;
    }
}

package com.tapioca.BE.adapter.out.mapper.Erd;

import com.tapioca.BE.adapter.out.entity.erd.AttributeEntity;
import com.tapioca.BE.adapter.out.entity.erd.AttributeLinkEntity;
import com.tapioca.BE.domain.model.erd.AttributeLink;
import org.springframework.stereotype.Component;

@Component
public class AttributeLinkMapper {
    public static AttributeLink toDomain(AttributeLinkEntity attributeLinkEntity) {
        return new AttributeLink(
                attributeLinkEntity.getId(),
                attributeLinkEntity.getFromAttribute().getId(),
                attributeLinkEntity.getToAttribute().getId(),
                attributeLinkEntity.getLinkType()
        );
    }

    public static AttributeLinkEntity toEntity(
            AttributeLink attributeLink,
            AttributeEntity fromEntity,
            AttributeEntity toEntity) {
        AttributeLinkEntity attributeLinkEntity= AttributeLinkEntity.builder()
                .id(attributeLink.getId())
                .linkType(attributeLink.getLinkType())
                .build();
        attributeLinkEntity.setFromAttribute(fromEntity);
        attributeLinkEntity.setToAttribute(toEntity);

        return attributeLinkEntity;
    }
}

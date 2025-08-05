package com.tapioca.BE.adapter.out.mapper.Erd;

import com.tapioca.BE.adapter.out.entity.erd.AttributeEntity;
import com.tapioca.BE.adapter.out.entity.erd.DiagramEntity;
import com.tapioca.BE.domain.model.erd.Attribute;
import org.springframework.stereotype.Component;

@Component
public class AttributeMapper {
    public static Attribute toDomain(AttributeEntity attributeEntity) {
        return new Attribute(
                attributeEntity.getId(),
                attributeEntity.getDiagram().getId(),
                attributeEntity.getName(),
                attributeEntity.getAttributeType(),
                attributeEntity.getLength(),
                attributeEntity.isPrimaryKey(),
                attributeEntity.isForeignKey()
        );
    }

    public static AttributeEntity toEntity(Attribute attribute, DiagramEntity diagramEntity) {
        AttributeEntity e = AttributeEntity.builder()
                .id(attribute.getId())
                .name(attribute.getName())
                .attributeType(attribute.getAttributeType())
                .length(attribute.getLength())
                .isPrimaryKey(attribute.isPrimaryKey())
                .isForeignKey(attribute.isForeignKey())
                .build();
        e.setDiagram(diagramEntity);
        return e;
    }
}
